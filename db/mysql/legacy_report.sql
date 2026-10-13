-- R4-N8 MySQL AES_ENCRYPT uses AES-128-ECB unless block_encryption_mode is changed; SHA1() for ids.
SET @key = 'test-only-key';
SELECT id, AES_ENCRYPT(card_number, @key) AS card_enc, SHA1(email) AS email_id FROM legacy_cards;
