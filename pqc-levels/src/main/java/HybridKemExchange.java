// R4-N5 Hand-built hybrid key exchange: ML-KEM-768 + ECDH P-256, both secrets combined with SHA-256.
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;

import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jcajce.spec.KEMGenerateSpec;
import org.bouncycastle.jcajce.spec.MLKEMParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HybridKemExchange {
    public static byte[] hybridSecret() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecureRandom rnd = new SecureRandom();

        KeyPairGenerator kemGen = KeyPairGenerator.getInstance("ML-KEM", "BC");
        kemGen.initialize(MLKEMParameterSpec.ml_kem_768, rnd);
        KeyPair kem = kemGen.generateKeyPair();
        KeyGenerator enc = KeyGenerator.getInstance("ML-KEM", "BC");
        enc.init(new KEMGenerateSpec(kem.getPublic(), "AES"), rnd);
        byte[] pqSecret = ((SecretKeyWithEncapsulation) enc.generateKey()).getEncoded();

        KeyPairGenerator ecGen = KeyPairGenerator.getInstance("EC");
        ecGen.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair a = ecGen.generateKeyPair(), b = ecGen.generateKeyPair();
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(a.getPrivate());
        ka.doPhase(b.getPublic(), true);
        byte[] ecSecret = ka.generateSecret();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(pqSecret);
        sha.update(ecSecret);
        return sha.digest();
    }
}
