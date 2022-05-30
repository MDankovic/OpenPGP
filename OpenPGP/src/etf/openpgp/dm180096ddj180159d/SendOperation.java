package etf.openpgp.dm180096ddj180159d;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

	@SuppressWarnings("resource")
	public void encryptMsg(String fileName, int secretKeyIndex, int publicKeyIndex, int encryptionAlgorithm,
			char[] passphrase, boolean bAuth, boolean bEncr, boolean bCompr, boolean bConv)
			throws PGPException, IOException {

		Security.addProvider(new BouncyCastleProvider());

		PGPSignatureGenerator sigGen = null;
		FileOutputStream fileOutStream = new FileOutputStream(fileName + ".gpg");
		OutputStream encOutStream = fileOutStream;

		if (bEncr) {
			PGPPublicKeyRing pkr = pubModel.getPublicKeyRingByIndex(publicKeyIndex);

			encryptionAlgorithm = SymmetricKeyAlgorithmTags.TRIPLE_DES;

			// Encryption
			PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
					new JcePGPDataEncryptorBuilder(encryptionAlgorithm).setWithIntegrityPacket(true)
							.setSecureRandom(new SecureRandom()).setProvider(new BouncyCastleProvider()));

			// Extraction of ElGamal public key
			PGPPublicKey encrKey = null;
			Iterator<PGPPublicKey> it = pkr.getPublicKeys();
			while (it.hasNext()) {
				encrKey = it.next();
				if (encrKey.isEncryptionKey())
					break;
			}

			if (encrKey != null) {
				encGen.addMethod(
						new JcePublicKeyKeyEncryptionMethodGenerator(encrKey).setProvider(new BouncyCastleProvider()));
				encOutStream = encGen.open(fileOutStream, new byte[1 << 16]);
			}

		}

		// Compression
		final PGPCompressedDataGenerator comGen = new PGPCompressedDataGenerator(
				bCompr ? CompressionAlgorithmTags.ZIP : CompressionAlgorithmTags.UNCOMPRESSED);
		OutputStream comOutStream = comGen.open(encOutStream, new byte[1 << 16]);

		if (bAuth) {
			// Signing
			PGPSecretKeyRing skr = secModel.getSecretKeyRingByIndex(secretKeyIndex);
			// Extraction of DSA private key
			PGPSecretKey secKey = skr.getSecretKey();
			PGPPrivateKey privKey = secModel.checkPasswordAndGetPrivateKey(skr, passphrase);

			sigGen = new PGPSignatureGenerator(
					new JcaPGPContentSignerBuilder(skr.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1));

			sigGen.init(PGPSignature.BINARY_DOCUMENT, privKey);

			String userId = secKey.getPublicKey().getUserIDs().next();
			PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();

			spGen.addSignerUserID(false, userId);
			sigGen.setHashedSubpackets(spGen.generate());
			sigGen.generateOnePassVersion(false).encode(comOutStream);
		}

		PGPLiteralDataGenerator litGen = new PGPLiteralDataGenerator();
		OutputStream litOutStream = litGen.open(encOutStream, PGPLiteralData.BINARY, fileName, 1 << 16, new Date());

		FileInputStream fileInStream = new FileInputStream(fileName);
		byte[] byteArray = new byte[1 << 16];
		int inputLen;
		while ((inputLen = fileInStream.read(byteArray)) > 0) {
			litOutStream.write(byteArray, 0, inputLen);
			
			if(bAuth) {
				sigGen.update(byteArray, 0, inputLen);
			}
		}
		fileInStream.close();
		litOutStream.close();
		
		if(bAuth) {
			sigGen.generate().encode(comOutStream);
		}
		
		if(bCompr) {
			comOutStream.close();	
		}
		
		if(bEncr) {
			encOutStream.close();	
		}
				
		
		fileOutStream.close();
	}
}
