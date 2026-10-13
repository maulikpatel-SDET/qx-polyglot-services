// R4-27 Every PQC parameter set: Qryptive should report the exact level, not just the family.
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.jcajce.spec.MLDSAParameterSpec;
import org.bouncycastle.jcajce.spec.MLKEMParameterSpec;
import org.bouncycastle.jcajce.spec.SLHDSAParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PqcLevels {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecureRandom rnd = new SecureRandom();

        KeyPairGenerator kem512 = KeyPairGenerator.getInstance("ML-KEM", "BC");
        kem512.initialize(MLKEMParameterSpec.ml_kem_512, rnd);
        KeyPairGenerator kem1024 = KeyPairGenerator.getInstance("ML-KEM", "BC");
        kem1024.initialize(MLKEMParameterSpec.ml_kem_1024, rnd);

        KeyPairGenerator dsa44 = KeyPairGenerator.getInstance("ML-DSA", "BC");
        dsa44.initialize(MLDSAParameterSpec.ml_dsa_44, rnd);
        KeyPairGenerator dsa87 = KeyPairGenerator.getInstance("ML-DSA", "BC");
        dsa87.initialize(MLDSAParameterSpec.ml_dsa_87, rnd);

        KeyPairGenerator slh = KeyPairGenerator.getInstance("SLH-DSA", "BC");
        slh.initialize(SLHDSAParameterSpec.slh_dsa_shake_256s, rnd);

        System.out.println(kem512.generateKeyPair().getPublic().getAlgorithm());
        System.out.println(kem1024.generateKeyPair().getPublic().getAlgorithm());
        System.out.println(dsa44.generateKeyPair().getPublic().getAlgorithm());
        System.out.println(dsa87.generateKeyPair().getPublic().getAlgorithm());
        System.out.println(slh.generateKeyPair().getPublic().getAlgorithm());
    }
}
