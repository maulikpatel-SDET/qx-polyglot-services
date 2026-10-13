"""R4-22 Python TLS client with explicit cipher list (RSA key exchange allowed) and TLS 1.0 minimum."""
import ssl

ctx = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
ctx.minimum_version = ssl.TLSVersion.TLSv1
ctx.set_ciphers("ECDHE-RSA-AES128-GCM-SHA256:AES128-SHA")
