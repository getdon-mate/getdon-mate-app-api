terraform {
  required_providers {
    oci = {
      source  = "oracle/oci"
      version = "~> 5.0"
    }
  }
  required_version = ">= 1.5"
}

provider "oci" {
  tenancy_ocid     = var.tenancy_ocid
  user_ocid        = var.user_ocid
  fingerprint      = var.fingerprint
  private_key_path = var.private_key_path
  region           = var.region
}

# ─── 네트워크 ───────────────────────────────────────────────

resource "oci_core_vcn" "main" {
  compartment_id = var.compartment_ocid
  cidr_block     = "10.0.0.0/16"
  display_name   = "getdon-vcn"
}

resource "oci_core_internet_gateway" "main" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.main.id
  display_name   = "getdon-igw"
  enabled        = true
}

resource "oci_core_route_table" "main" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.main.id
  display_name   = "getdon-rt"

  route_rules {
    destination       = "0.0.0.0/0"
    network_entity_id = oci_core_internet_gateway.main.id
  }
}

resource "oci_core_security_list" "main" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.main.id
  display_name   = "getdon-sl"

  # 아웃바운드 전체 허용
  egress_security_rules {
    protocol    = "all"
    destination = "0.0.0.0/0"
  }

  # SSH
  ingress_security_rules {
    protocol = "6"
    source   = "0.0.0.0/0"
    tcp_options {
      min = 22
      max = 22
    }
  }

  # API 서버
  ingress_security_rules {
    protocol = "6"
    source   = "0.0.0.0/0"
    tcp_options {
      min = 8080
      max = 8080
    }
  }

  # HTTPS
  ingress_security_rules {
    protocol = "6"
    source   = "0.0.0.0/0"
    tcp_options {
      min = 443
      max = 443
    }
  }

  # HTTP
  ingress_security_rules {
    protocol = "6"
    source   = "0.0.0.0/0"
    tcp_options {
      min = 80
      max = 80
    }
  }
}

resource "oci_core_subnet" "main" {
  compartment_id    = var.compartment_ocid
  vcn_id            = oci_core_vcn.main.id
  cidr_block        = "10.0.1.0/24"
  display_name      = "getdon-subnet"
  route_table_id    = oci_core_route_table.main.id
  security_list_ids = [oci_core_security_list.main.id]
}

# ─── Compute 인스턴스 (OCI Free Tier ARM) ───────────────────

resource "oci_core_instance" "app" {
  compartment_id      = var.compartment_ocid
  availability_domain = var.availability_domain
  display_name        = "getdon-api-server"

  # Free Tier: Ampere A1 Flex (4 OCPU / 24 GB 무료)
  shape = "VM.Standard.A1.Flex"
  shape_config {
    ocpus         = 2
    memory_in_gbs = 12
  }

  source_details {
    source_type = "image"
    source_id   = var.instance_image_ocid
  }

  create_vnic_details {
    subnet_id        = oci_core_subnet.main.id
    assign_public_ip = true
    display_name     = "getdon-api-vnic"
  }

  metadata = {
    ssh_authorized_keys = var.ssh_public_key
  }

  # Docker + PostgreSQL 초기 설치
  user_data = base64encode(<<-EOF
    #!/bin/bash
    apt-get update -y
    apt-get install -y docker.io postgresql postgresql-contrib

    # Docker 서비스 시작
    systemctl enable docker
    systemctl start docker

    # PostgreSQL 설정
    systemctl enable postgresql
    systemctl start postgresql
    sudo -u postgres psql -c "CREATE USER getdon WITH PASSWORD '${var.db_password}';"
    sudo -u postgres psql -c "CREATE DATABASE getdondb OWNER getdon;"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE getdondb TO getdon;"
  EOF
  )
}