package etf.openpgp.dm180096ddj180159d;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.table.DefaultTableModel;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

@SuppressWarnings("serial")
public class KeyRingTableModel extends DefaultTableModel {

	private String[] columns = { "Name", "E-Mail", "Valid From", "Valid Until", "Key-ID" };

	private List<PGPSecretKeyRing> keyRingCollection = new ArrayList<>();

	public KeyRingTableModel() {
		super();

		this.setColumnIdentifiers(columns);
	}

	public void addKeyRing(PGPSecretKeyRing skr) {
		this.keyRingCollection.add(skr);

		String userId = skr.getPublicKey().getUserIDs().next();
		String keyId = Long.toHexString(skr.getPublicKey().getKeyID());
		Date keyCreationDate = skr.getPublicKey().getCreationTime();

		// Because of out representation
		String username = userId.split(" <")[0];
		String email = userId.split(" <")[1];
		StringBuffer sb = new StringBuffer(email);
		sb.deleteCharAt(sb.length() - 1);
		email = sb.toString();
		
		keyId = Long.toHexString(skr.getPublicKey().getKeyID()).toUpperCase();
		StringBuilder sbuild = new StringBuilder();

		sbuild.append(keyId.substring(0, 4)).append(" ").append(keyId.substring(4, 8)).append(" ")
				.append(keyId.substring(8, 12)).append(" ").append(keyId.substring(12, 16));
		keyId = sbuild.toString();
		
		addRow(new Object[] { username, email, keyCreationDate, "xxx", keyId });
	}

	public void exportKeyRing(int index) {
		PGPSecretKeyRing skr = this.keyRingCollection.get(index - 6);

		String userId = skr.getPublicKey().getUserIDs().next();
		// Because of out representation
		String username = userId.split(" <")[0];
		String email = userId.split(" <")[1];
		StringBuffer sbuff = new StringBuffer(email);
		sbuff.deleteCharAt(sbuff.length() - 1);
		email = sbuff.toString();

		String keyId = Long.toHexString(skr.getPublicKey().getKeyID()).toUpperCase();
		String filepath = username + "_" + email + "-" + keyId;

		try (ArmoredOutputStream outPrivate = new ArmoredOutputStream(
				new FileOutputStream(filepath + "-secret.asc"));) {
			skr.encode(outPrivate);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterable<PGPPublicKey> iterable = () -> skr.getPublicKeys();
		List<PGPPublicKey> publicKeys = StreamSupport.stream(iterable.spliterator(), false)
				.collect(Collectors.toList());

		PGPPublicKeyRing pkr = new PGPPublicKeyRing(publicKeys);
		try (ArmoredOutputStream outPublic = new ArmoredOutputStream(new FileOutputStream(filepath + "-public.asc"));) {
			pkr.encode(outPublic);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
