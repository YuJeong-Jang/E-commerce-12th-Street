# 1. Route 53 호스트 존 생성
resource "aws_route53_zone" "main" {
  name = "12-streets.store"
}

# 2. 이미 발급 완료된(ISSUED) 인증서 정보 가져오기
# (인증서 요청, DNS 레코드 생성, 검증 대기 과정을 모두 생략합니다)
data "aws_acm_certificate" "cert" {
  domain      = "12-streets.store"
  statuses    = ["ISSUED"]
  most_recent = true
}

# 3. (중요) 다른 리소스에서 ARN을 참조할 때 수정법
# 기존: aws_acm_certificate_validation.cert.certificate_arn
# 변경: data.aws_acm_certificate.cert.arn