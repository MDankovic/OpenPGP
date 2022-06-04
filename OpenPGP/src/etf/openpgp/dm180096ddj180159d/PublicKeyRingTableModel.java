package etf.openpgp.dm180096ddj180159d;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;

@SuppressWarnings("serial")
public class PublicKeyRingTableModel extends DefaultTableModel {
	private String[] columns = { "Name", "E-Mail", "Valid From", "Key-ID" };

	private List<PGPPublicKeyRing> publicKeyRingList = new ArrayList<>();

	public PublicKeyRingTableModel() {
		super();

		this.setColumnIdentifiers(columns);
	}

	public void addKeyRing(PGPPublicKeyRing pkr) throws IllegalValueException {

		int size = this.publicKeyRingList.size();
		for (int i = 0; i < size; i++) {
			if (this.publicKeyRingList.get(i).getPublicKey().getKeyID() == pkr.getPublicKey().getKeyID()) {
				throw new IllegalValueException("Public key already exists.");
			}
		}

		this.publicKeyRingList.add(pkr);

		String userId = pkr.getPublicKey().getUserIDs().next();
		String keyId = Long.toHexString(pkr.getPublicKey().getKeyID());
		Date keyCreationDate = pkr.getPublicKey().getCreationTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = formatter.format(keyCreationDate);

		// Because of out representation
		String username = userId.split(" <")[0];
		String email = userId.split(" <")[1];
		email = new StringBuilder(email).deleteCharAt(email.length() - 1).toString();

		keyId = Long.toHexString(pkr.getPublicKey().getKeyID()).toUpperCase();
		StringBuilder sbuild = new StringBuilder();

		for (int i = keyId.length(); i < 16; i++) {
			keyId = "0" + keyId;
		}

		for (int i = 0; i < 16; i += 4) {
			sbuild.append(keyId.substring(i, i + 4) + " ");
		}
		keyId = sbuild.deleteCharAt(sbuild.length() - 1).toString();

		addRow(new Object[] { username, email, strDate, keyId });
	}

	public PGPPublicKeyRingCollection getKeyRingCollection() throws IOException, PGPException {
		return new PGPPublicKeyRingCollection(publicKeyRingList);
	}

	public void setKeyRingList(PGPPublicKeyRingCollection keyRingCollection) throws IllegalValueException {
		setRowCount(0);
		this.addKeyRingList(keyRingCollection);
	}

	public void addKeyRingList(PGPPublicKeyRingCollection keyRingCollection) throws IllegalValueException {
		List<PGPPublicKeyRing> keyRingList = new ArrayList<>();
		Iterator<PGPPublicKeyRing> it = keyRingCollection.getKeyRings();
		while (it.hasNext()) {

			keyRingList.add(it.next());
		}

		for (PGPPublicKeyRing pkr : keyRingList) {
			addKeyRing(pkr);
		}
	}

	public void removeKeyRing(int index) {
		this.publicKeyRingList.remove(index);
		removeRow(index);
	}

	public PGPPublicKeyRing getPublicKeyRingByIndex(int index) {
		return this.publicKeyRingList.get(index);
	}

	public String getPublicKeyString(int index) {
		PGPPublicKeyRing pkr = this.publicKeyRingList.get(index);
		String userId = pkr.getPublicKey().getUserIDs().next();
		String keyId = Long.toHexString(pkr.getPublicKey().getKeyID());

		return userId + "/" + keyId;
	}

	public List<PGPPublicKeyRing> getPublicKeyRingsByIndexes(List<Integer> indexes) {
		List<PGPPublicKeyRing> list = new ArrayList<>();
		for (Integer i : indexes) {
			list.add(this.publicKeyRingList.get(i));
		}
		return list;
	}

}
