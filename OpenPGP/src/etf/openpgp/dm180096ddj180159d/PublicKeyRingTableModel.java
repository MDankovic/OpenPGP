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
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

@SuppressWarnings("serial")
public class PublicKeyRingTableModel extends DefaultTableModel {
	private String[] columns = { "Name", "E-Mail", "Valid From", "Key-ID" };

	private List<PGPPublicKeyRing> publicKeyRingList = new ArrayList<>();

	public PublicKeyRingTableModel() {
		super();

		this.setColumnIdentifiers(columns);
	}

	public void addKeyRing(PGPPublicKeyRing pkr) {
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

		for (int i = 0; i < 16; i += 4) {
			sbuild.append(keyId.substring(i, i + 4) + " ");
		}
		keyId = sbuild.deleteCharAt(sbuild.length() - 1).toString();

		addRow(new Object[] { username, email, strDate, keyId });
	}

	public PGPPublicKeyRingCollection getKeyRingCollection() throws IOException, PGPException {
		return new PGPPublicKeyRingCollection(publicKeyRingList);
	}

	public void setKeyRingList(PGPPublicKeyRingCollection keyRingCollection) {
		setRowCount(0);
		this.addKeyRingList(keyRingCollection);
	}

	public void addKeyRingList(PGPPublicKeyRingCollection keyRingCollection) {
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

}