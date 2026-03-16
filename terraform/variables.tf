variable "tenancy_ocid" {
  description = "OCI 테넌시 OCID"
  type        = string
}

variable "user_ocid" {
  description = "OCI 사용자 OCID"
  type        = string
}

variable "fingerprint" {
  description = "OCI API 키 fingerprint"
  type        = string
}

variable "private_key_path" {
  description = "OCI API 개인키 파일 경로"
  type        = string
  default     = "~/.oci/oci_api_key.pem"
}

variable "region" {
  description = "OCI 리전"
  type        = string
  default     = "ap-chuncheon-1"
}

variable "compartment_ocid" {
  description = "OCI 컴파트먼트 OCID"
  type        = string
}

variable "availability_domain" {
  description = "가용 도메인 (예: IYfK:AP-CHUNCHEON-1-AD-1)"
  type        = string
}

variable "instance_image_ocid" {
  description = "Compute 인스턴스 OS 이미지 OCID (Ubuntu 22.04 권장)"
  type        = string
}

variable "ssh_public_key" {
  description = "인스턴스 접속용 SSH 공개키"
  type        = string
}

variable "db_password" {
  description = "PostgreSQL 비밀번호"
  type        = string
  sensitive   = true
}
