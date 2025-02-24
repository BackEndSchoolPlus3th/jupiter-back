variable "prefix" {
  description = "Prefix for all resources"
  default     = "dev"
}

variable "region" {
  description = "NCP region"
  default     = "KR"
}

variable "zone" {
  description = "NCP zone"
  default     = "KR-1"
}

variable "access_key" {
  description = "NCP API Access Key"
  type        = string
  sensitive   = true
}

variable "secret_key" {
  description = "NCP API Secret Key"
  type        = string
  sensitive   = true
}

variable "login_key_name" {
  description = "Name of the SSH key pair"
  type        = string
}

variable "server_image_product_code" {
  description = "Server image product code"
  type        = string
  default     = "SW.VSVR.OS.LNX64.UBNTU.SVR2004.B050"  # Ubuntu 20.04
}

variable "server_product_code" {
  description = "Server product code"
  type        = string
  default     = "SVR.VSVR.STAND.C002.M008.NET.HDD.B050.G002"  # 2vCPU, 8GB RAM
}