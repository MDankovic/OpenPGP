package etf.openpgp.dm180096ddj180159d;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.util.io.Streams;

public class ReceiveOperation {

	private SecretKeyRingTableModel secModel;
	private PublicKeyRingTableModel pubModel;

	public ReceiveOperation(SecretKeyRingTableModel secModel, PublicKeyRingTableModel pubModel) {
		this.secModel = secModel;
		this.pubModel = pubModel;
	}

	public void receiveMsg(String fileNameIn, char[] passphrase) throws PGPException, IOException {

		String fileNameOut = fileNameIn.substring(0, fileNameIn.length() - 4) + ".txt";

		InputStream fileInStream = PGPUtil.getDecoderStream(new FileInputStream(fileNameIn));

		PGPObjectFactory factory = new PGPObjectFactory(fileInStream, new BcKeyFingerprintCalculator());

		Object obj = factory.nextObject();

		PGPEncryptedDataList encDataList = (PGPEncryptedDataList) (obj instanceof PGPEncryptedDataList ? obj
				: factory.nextObject());

		PGPPublicKeyEncryptedData encData = null;
		PGPPrivateKey privKey = null;
		for (Iterator<PGPEncryptedData> iter = encDataList.getEncryptedDataObjects(); privKey == null
				&& iter.hasNext();) {
			encData = (PGPPublicKeyEncryptedData) iter.next();
			System.out.println(Long.toHexString(encData.getKeyID()));
			privKey = secModel.checkPasswordAndGetPrivateKeyEncryption(encData.getKeyID(), passphrase);
		}

		if (privKey == null) {
			fileInStream.close();
			throw new PGPException("Private key not found.");
		}

		// Decrypt data
		InputStream decryptedInputStream = encData.getDataStream(
				new JcePublicKeyDataDecryptorFactoryBuilder().setProvider(new BouncyCastleProvider()).build(privKey));
		factory = new PGPObjectFactory(decryptedInputStream, new BcKeyFingerprintCalculator());
		obj = factory.nextObject();

		// Uncompress data
		if (obj instanceof PGPCompressedData) {
			factory = new PGPObjectFactory(((PGPCompressedData) obj).getDataStream(), new BcKeyFingerprintCalculator());
			obj = factory.nextObject();
		}

		// Get one pass signature list
		PGPOnePassSignatureList onePassSignList = null;
		if (obj instanceof PGPOnePassSignatureList) {
			onePassSignList = (PGPOnePassSignatureList) obj;
			obj = factory.nextObject();
		}

		// Literal data
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();

		if (obj instanceof PGPLiteralData) {
			Streams.pipeAll(((PGPLiteralData) obj).getInputStream(), byteOutStream);
			obj = factory.nextObject();
		}

		PGPSignatureList signList = null;
		if (obj instanceof PGPSignatureList) {
			signList = (PGPSignatureList) obj;
		}

//		int len;
//		byte[] byteArr = new byte[1 << 16];
//		while ((len = fileInStream.read(byteArr)) > 0) {
//			byteOutStream.write(byteArr);
//		}
		

		
		OutputStream fileOutStream = new FileOutputStream(fileNameOut);
		fileOutStream.write(byteOutStream.toByteArray());
		
		byte[] msg = byteOutStream.toByteArray();
		byteOutStream.close();

		fileInStream.close();
		fileOutStream.close();
		
		System.out.println(msg);
		
		if (onePassSignList == null || signList == null) {
			System.out.println("No signature.");
			return;
		}

		PGPOnePassSignature onePassSign = onePassSignList.get(0);
		PGPPublicKey pubKey = pubModel.getKeyRingCollection().getPublicKey(onePassSign.getKeyID());

		onePassSign.init(new JcaPGPContentVerifierBuilderProvider().setProvider(new BouncyCastleProvider()), pubKey);
		onePassSign.update(msg);

		String info = pubKey.getUserIDs().next();
//		System.out.println(info);

		if (onePassSign.verify(signList.get(0))) {
			System.out.println("Signed by: " + info);
		} else {
			System.out.println("Signature not verified");
		}

	}

}
