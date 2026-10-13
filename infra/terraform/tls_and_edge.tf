# R4-N9 Keys and TLS settings created by Terraform: tls provider, ACM, load balancer, CloudFront.

resource "tls_private_key" "internal_ca" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "tls_private_key" "service" {
  algorithm   = "ECDSA"
  ecdsa_curve = "P384"
}

resource "tls_self_signed_cert" "internal_ca" {
  private_key_pem       = tls_private_key.internal_ca.private_key_pem
  validity_period_hours = 87600
  is_ca_certificate     = true
  allowed_uses          = ["cert_signing", "crl_signing"]
  subject {
    common_name  = "QX Internal CA"
    organization = "QX Test Only"
  }
}

resource "aws_acm_certificate" "api" {
  domain_name       = "api.qx.example.com"
  validation_method = "DNS"
  key_algorithm     = "RSA_2048"
}

resource "aws_lb_listener" "https" {
  load_balancer_arn = "arn:aws:elasticloadbalancing:ap-south-1:000000000000:loadbalancer/app/qx-test/0000000000000000"
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = aws_acm_certificate.api.arn
  default_action {
    type             = "forward"
    target_group_arn = "arn:aws:elasticloadbalancing:ap-south-1:000000000000:targetgroup/qx-test/0000000000000000"
  }
}

resource "aws_cloudfront_distribution" "web" {
  enabled = true
  origin {
    domain_name = "origin.qx.example.com"
    origin_id   = "qx-origin"
    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "https-only"
      origin_ssl_protocols   = ["TLSv1", "TLSv1.2"]
    }
  }
  default_cache_behavior {
    target_origin_id       = "qx-origin"
    viewer_protocol_policy = "redirect-to-https"
    allowed_methods        = ["GET", "HEAD"]
    cached_methods         = ["GET", "HEAD"]
    forwarded_values {
      query_string = false
      cookies { forward = "none" }
    }
  }
  restrictions {
    geo_restriction { restriction_type = "none" }
  }
  viewer_certificate {
    acm_certificate_arn      = aws_acm_certificate.api.arn
    ssl_support_method       = "sni-only"
    minimum_protocol_version = "TLSv1"
  }
}
