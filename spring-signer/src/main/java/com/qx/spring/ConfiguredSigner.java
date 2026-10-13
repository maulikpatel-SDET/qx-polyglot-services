// R4-N7 Spring component: algorithm names come only from application.properties.
package com.qx.spring;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfiguredSigner {
    @Value("${qx.crypto.key-algorithm}")
    private String keyAlgorithm;

    @Value("${qx.crypto.curve}")
    private String curve;

    @Value("${qx.crypto.signature-algorithm}")
    private String signatureAlgorithm;

    public byte[] sign(byte[] data) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyAlgorithm);
        kpg.initialize(new ECGenParameterSpec(curve));
        KeyPair kp = kpg.generateKeyPair();
        Signature s = Signature.getInstance(signatureAlgorithm);
        s.initSign(kp.getPrivate());
        s.update(data);
        return s.sign();
    }
}
