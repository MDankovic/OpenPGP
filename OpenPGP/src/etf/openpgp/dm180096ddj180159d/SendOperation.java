package etf.openpgp.dm180096ddj180159d;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

public class SendOperation {

	private SecretKeyRingTableModel secModel;
	private PublicKeyRingTableModel pubModel;

	public SendOperation(SecretKeyRingTableModel secModel, PublicKeyRingTableModel pubModel) {
		this.secModel = secModel;
		this.pubModel = pubModel;
	}

	public void encryptMsg(String fileName, int secretKeyIndex, List<Integer> publicKeyIndexes, int encryptionAlgorithm,
			char[] passphrase) throws PGPException, IOException {

		PGPSecretKeyRing skr = secModel.getSecretKeyRingByIndex(secretKeyIndex);
		List<PGPPublicKeyRing> pkrList = pubModel.getPublicKeyRingsByIndexes(publicKeyIndexes);

		// Signing process.
		// Extraction of DSA private key
		PGPSecretKey secKey = skr.getSecretKey();
		PGPPrivateKey privKey = secModel.checkPasswordAndGetPrivateKey(skr, passphrase);

		PGPSignatureGenerator sigGen = new PGPSignatureGenerator(
				new JcaPGPContentSignerBuilder(skr.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA256));

		sigGen.init(PGPSignature.BINARY_DOCUMENT, privKey);

		String userId = secKey.getPublicKey().getUserIDs().next();
		PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();

		spGen.addSignerUserID(false, userId);
		sigGen.setHashedSubpackets(spGen.generate());

		
	    // Compress
		//??
		
	    byte[] bytes = new byte[2000];
//		compressFile(fileName, CompressionAlgorithmTags.ZIP);
	    
	    
		// Encryption process.
		PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
				new JcePGPDataEncryptorBuilder(encryptionAlgorithm).setWithIntegrityPacket(true)
						.setSecureRandom(new SecureRandom()).setProvider("BC"));

		for (PGPPublicKeyRing pkr : pkrList) {
			// Extraction of ElGamal public key
			Iterator<PGPPublicKey> it = pkr.getPublicKeys();
			it.next();
			PGPPublicKey pubKey = it.next();
			encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey).setProvider("BC"));
		}

		// nesto sa strimovima
	    ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();
	    OutputStream encOut = encGen.open(encryptedOutputStream, bytes);
	    encOut.write(bytes);
	    encOut.close();
	    byte[] bytesEncrypted = encryptedOutputStream.toByteArray();
	    encryptedOutputStream.close();

		// Compression
		final PGPCompressedDataGenerator comGen = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
		final PGPLiteralDataGenerator litGen = new PGPLiteralDataGenerator();
		final byte[] buffer = new byte[1 << 16];
		final OutputStream pOut = litGen.open(comGen.open(encOut), PGPLiteralData.BINARY, "", new Date(), buffer);

		try (ArmoredOutputStream out = new ArmoredOutputStream(
				new FileOutputStream(SecretKeyRingTableModel.generateFilePath(skr) + "-sent_message.asc"));) {

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
