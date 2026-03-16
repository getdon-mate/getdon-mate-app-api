output "instance_public_ip" {
  description = "서버 퍼블릭 IP"
  value       = oci_core_instance.app.public_ip
}

output "instance_id" {
  description = "Compute 인스턴스 OCID"
  value       = oci_core_instance.app.id
}

output "vcn_id" {
  description = "VCN OCID"
  value       = oci_core_vcn.main.id
}

output "ssh_command" {
  description = "SSH 접속 명령어"
  value       = "ssh ubuntu@${oci_core_instance.app.public_ip}"
}
