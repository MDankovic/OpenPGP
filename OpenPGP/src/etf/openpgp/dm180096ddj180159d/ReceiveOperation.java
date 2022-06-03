package etf.openpgp.dm180096ddj180159d;

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
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
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
		
		PGPEncryptedDataList encDataList = (PGPEncryptedDataList)(obj instanceof PGPEncryptedDataList ? obj : factory.nextObject());
		
		PGPPublicKeyEncryptedData encData = null;
		PGPPrivateKey privKey = null;
		for (Iterator<PGPEncryptedData> iter = encDataList.getEncryptedDataObjects(); privKey == null && iter.hasNext();) {
			encData = (PGPPublicKeyEncryptedData)iter.next();
			privKey = secModel.checkPasswordAndGetPrivateKey(encData.getKeyID(), passphrase);
		}
		
		if (privKey == null) {
			fileInStream.close();
			throw new PGPException("Private key not found");
		}
		
		// Decrypt data
		InputStream decryptedInputStream = encData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider(new BouncyCastleProvider()).build(privKey));
		factory = new PGPObjectFactory(decryptedInputStream, new BcKeyFingerprintCalculator());
		obj = factory.nextObject();
		
		// Uncompress data
		if(obj instanceof PGPCompressedData) {
			factory = new PGPObjectFactory(((PGPCompressedData)obj).getDataStream(), new BcKeyFingerprintCalculator());
			obj = factory.nextObject();
		} 

		OutputStream fileOutStream = new FileOutputStream(fileNameOut);
		if(obj instanceof PGPLiteralData) {
			Streams.pipeAll(((PGPLiteralData) obj).getInputStream(), fileOutStream);
		}
		

		int len;
		byte[] byteArr = new byte[1 << 16];
		while ((len = fileInStream.read(byteArr)) > 0) {
			fileOutStream.write(byteArr, 0, len);
		}

		fileInStream.close();
		fileOutStream.close();
	}

}
