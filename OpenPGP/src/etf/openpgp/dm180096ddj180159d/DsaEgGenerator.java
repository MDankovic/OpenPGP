package etf.openpgp.dm180096ddj180159d;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

public class DsaEgGenerator {

	private static PGPKeyRingGenerator generateKeyRingGenerator(String identity, String passPhrase, boolean armor,
			int dsaKeySize, int elGamalKeySize) throws NoSuchAlgorithmException, NoSuchProviderException, PGPException {

		KeyPairGenerator egGen = KeyPairGenerator.getInstance("ELGAMAL", "BC");
		KeyPairGenerator dsaGen = KeyPairGenerator.getInstance("DSA", "BC");

		dsaGen.initialize(dsaKeySize);
		egGen.initialize(elGamalKeySize);

		PGPKeyPair dsaKeyPair = new JcaPGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, dsaGen.generateKeyPair(), new Date());
		PGPKeyPair egKeyPair = new JcaPGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, egGen.generateKeyPair(), new Date());

		PGPDigestCalculator sha1Calculator = new JcaPGPDigestCalculatorProviderBuilder().build()
				.get(HashAlgorithmTags.SHA1);
		PGPContentSignerBuilder csBuilder = new JcaPGPContentSignerBuilder(dsaKeyPair.getPublicKey().getAlgorithm(),
				HashAlgorithmTags.SHA1);
		PBESecretKeyEncryptor skEncryptor = new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calculator)
				.setProvider("BC").build(passPhrase.toCharArray());

		PGPKeyRingGenerator krg = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaKeyPair, identity,
				sha1Calculator, null, null, csBuilder, skEncryptor);

		krg.addSubKey(egKeyPair);
		return krg;
	}

	public static PGPSecretKeyRing generateKeyRing(String identity, String passPhrase, boolean armor, int dsaKeySize,
			int elGamalKeySize) throws NoSuchAlgorithmException, NoSuchProviderException, PGPException {

		PGPKeyRingGenerator keyRingGen = generateKeyRingGenerator(identity, passPhrase, armor, dsaKeySize, elGamalKeySize);
		return keyRingGen.generateSecretKeyRing();
	}

}
