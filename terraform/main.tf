# main.tf

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      # 6.0.0 미만 제약 조건 충돌을 피하기 위해 5.x 버전 중 최신 사용
      version = "~> 5.0" 
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}

# VPC 구성
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"

  name = "eks-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["ap-northeast-2a", "ap-northeast-2c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true

  public_subnet_tags = {
    "kubernetes.io/role/elb" = 1
  }

  private_subnet_tags = {
    "kubernetes.io/role/internal-elb" = 1
  }
}

# EKS 구성 (v20 시리즈는 매우 안정적이며 인자 이름이 확실합니다)
module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 20.0"

  cluster_name    = "my-eks-cluster"
  cluster_version = "1.32" # 1.31보다 권장되는 안정 버전

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets
  
  cluster_endpoint_public_access = true

  eks_managed_node_groups = {
    general = {
      instance_types = ["t3.medium"]
      min_size     = 2
      max_size     = 2
      desired_size = 2 # 요청하신 대로 2대 고정
    }
  }

  # 클러스터 생성자 권한 부여
  enable_cluster_creator_admin_permissions = true

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }
}