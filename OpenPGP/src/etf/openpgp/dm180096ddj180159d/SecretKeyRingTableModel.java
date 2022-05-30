package etf.openpgp.dm180096ddj180159d;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.table.DefaultTableModel;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

@SuppressWarnings("serial")
public class SecretKeyRingTableModel extends DefaultTableModel {

	private String[] columns = { "Name", "E-Mail", "Valid From", "Key-ID" };

	private List<PGPSecretKeyRing> secretKeyRingList = new ArrayList<>();

	public SecretKeyRingTableModel() {
		super();

		this.setColumnIdentifiers(columns);
	}

	public void addKeyRing(PGPSecretKeyRing skr) {
		this.secretKeyRingList.add(skr);

		String userId = skr.getPublicKey().getUserIDs().next();
		String keyId = Long.toHexString(skr.getPublicKey().getKeyID());
		Date keyCreationDate = skr.getPublicKey().getCreationTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = formatter.format(keyCreationDate);

		// Because of out representation
		String username = userId.split(" <")[0];
		String email = userId.split(" <")[1];
		email = new StringBuilder(email).deleteCharAt(email.length() - 1).toString();

		keyId = Long.toHexString(skr.getPublicKey().getKeyID()).toUpperCase();
		StringBuilder sbuild = new StringBuilder();

		for (int i = keyId.length(); i < 16; i++) {
			sbuild.append('0');
		}

		for (int i = 0; i < 16; i += 4) {
			sbuild.append(keyId.substring(i, i + 4) + " ");
		}
		keyId = sbuild.deleteCharAt(sbuild.length() - 1).toString();

		addRow(new Object[] { username, email, strDate, keyId });
	}

	public void exportPublicKey(int index) {
		PGPSecretKeyRing skr = this.secretKeyRingList.get(index);
		PGPPublicKeyRing pkr = this.getPublicKeyRing(skr);

		try (ArmoredOutputStream outPublic = new ArmoredOutputStream(
				new FileOutputStream(generateFilePath(skr) + "-public.asc"));) {
			pkr.encode(outPublic);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void backupSecretKey(int index) {
		PGPSecretKeyRing skr = this.secretKeyRingList.get(index);

		try (ArmoredOutputStream outSecret = new ArmoredOutputStream(
				new FileOutputStream(generateFilePath(skr) + "-secret.asc"));) {
			skr.encode(outSecret);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String generateFilePath(PGPSecretKeyRing skr) {
		// Because of out representation
		String userId = skr.getPublicKey().getUserIDs().next();
		String username = userId.split(" <")[0];
		String email = userId.split(" <")[1];
		email = new StringBuilder(email).deleteCharAt(email.length() - 1).toString();

		String keyId = Long.toHexString(skr.getPublicKey().getKeyID()).toUpperCase();
		return username + "_" + email + "-" + keyId;
	}

	public PGPSecretKeyRingCollection getKeyRingCollection() throws IOException, PGPException {
		return new PGPSecretKeyRingCollection(secretKeyRingList);
	}

	public void setKeyRingList(PGPSecretKeyRingCollection keyRingCollection) {
		setRowCount(0);
		this.addKeyRingList(keyRingCollection);
	}

	public void addKeyRingList(PGPSecretKeyRingCollection keyRingCollection) {
		List<PGPSecretKeyRing> keyRingList = new ArrayList<>();
		Iterator<PGPSecretKeyRing> it = keyRingCollection.getKeyRings();
		while (it.hasNext()) {
			keyRingList.add(it.next());
		}

		for (PGPSecretKeyRing skr : keyRingList) {
			addKeyRing(skr);
		}
	}

	public void removeKeyRing(int index, char[] passphrase) throws PGPException {
		checkPasswordAndGetPrivateKey(this.secretKeyRingList.get(index), passphrase);

		this.secretKeyRingList.remove(index);
		removeRow(index);
	}

	private PGPPublicKeyRing getPublicKeyRing(PGPSecretKeyRing skr) {
		Iterable<PGPPublicKey> iterable = () -> skr.getPublicKeys();
		List<PGPPublicKey> publicKeys = StreamSupport.stream(iterable.spliterator(), false)
				.collect(Collectors.toList());

		return new PGPPublicKeyRing(publicKeys);
	}

	public PGPSecretKeyRing getSecretKeyRingByIndex(int index) {
		return this.secretKeyRingList.get(index);
	}

	public String getSecretKeyString(int index) {
		PGPSecretKeyRing skr = this.secretKeyRingList.get(index);
		String userId = skr.getPublicKey().getUserIDs().next();
		String keyId = Long.toHexString(skr.getPublicKey().getKeyID());

		return userId + "/" + keyId;
	}

	public PGPPrivateKey checkPasswordAndGetPrivateKey(PGPSecretKeyRing skr, char[] passphrase) throws PGPException {
		PBESecretKeyDecryptor decryptorFactory = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider())
				.build(passphrase);
		return skr.getSecretKey().extractPrivateKey(decryptorFactory);
	}
}
