-- R4-N8 Crypto inside the database layer.
-- PostgreSQL pgcrypto: PGP public-key encryption (the public key is an RSA key stored in a table)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE customers
SET    pan_encrypted = pgp_pub_encrypt(pan_plain, dearmor((SELECT armored_rsa_public FROM crypto_keys WHERE kid = 'qx-pgp-rsa-2048')))
WHERE  pan_encrypted IS NULL;

-- PostgreSQL pgcrypto: symmetric with 3DES (weak) and MD5 password hash (weak)
UPDATE partners SET secret_enc = encrypt(secret_plain::bytea, 'test-only-key', '3des');
UPDATE users    SET pw_hash    = crypt(password_plain, gen_salt('md5'));
