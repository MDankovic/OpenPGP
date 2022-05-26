package etf.openpgp.dm180096ddj180159d;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.table.DefaultTableModel;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;

@SuppressWarnings("serial")
public class KeyRingTableModel extends DefaultTableModel {

	private String[] columns = { "Name", "E-Mail", "Valid From", "Key-ID" };

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
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	    String strDate = formatter.format(keyCreationDate);  

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
		
		addRow(new Object[] { username, email, strDate, keyId });
	}

	public void exportKeyRing(int index) {
		PGPSecretKeyRing skr = this.keyRingCollection.get(index);

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
	
	public PGPSecretKeyRingCollection getSecretRingCollection() throws IOException, PGPException {
		return new PGPSecretKeyRingCollection(keyRingCollection);
	}
	
	public PGPPublicKeyRingCollection getPublicKeyRingCollection() throws IOException, PGPException {
		List<PGPPublicKeyRing> publicKeyRingCollection = new ArrayList<>();
		
		for(PGPSecretKeyRing skr : this.keyRingCollection) {
			Iterable<PGPPublicKey> iterable = () -> skr.getPublicKeys();
			List<PGPPublicKey> publicKeys = StreamSupport.stream(iterable.spliterator(), false)
					.collect(Collectors.toList());

			PGPPublicKeyRing pkr = new PGPPublicKeyRing(publicKeys);
			
			publicKeyRingCollection.add(pkr);
		}
		
		return new PGPPublicKeyRingCollection(publicKeyRingCollection);
	}

	public List<PGPSecretKeyRing> getKeyRingCollection() {
		return keyRingCollection;
	}

	public void setKeyRingCollection(List<PGPSecretKeyRing> keyRingCollection) {
		setRowCount(0);
		for(PGPSecretKeyRing skr : keyRingCollection) {
			addKeyRing(skr);
		}
	}

	@Override
	public void removeRow(int row) {
		super.removeRow(row);
		this.keyRingCollection.remove(row);
	}
	
}
