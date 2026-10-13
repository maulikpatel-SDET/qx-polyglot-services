// R4-N6 Issues an X.509 certificate signed with ML-DSA-65 (post-quantum PKI).
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jcajce.spec.MLDSAParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class MlDsaCertificateIssuer {
    public static X509Certificate issue() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-DSA", "BC");
        kpg.initialize(MLDSAParameterSpec.ml_dsa_65, new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();

        X500Name name = new X500Name("CN=pqc.qx.example.com, O=QX Test Only");
        Date from = new Date(), to = new Date(from.getTime() + 365L * 24 * 3600 * 1000);
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                name, BigInteger.valueOf(System.currentTimeMillis()), from, to, name, kp.getPublic());
        ContentSigner signer = new JcaContentSignerBuilder("ML-DSA").setProvider("BC").build(kp.getPrivate());
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(builder.build(signer));
    }
}
