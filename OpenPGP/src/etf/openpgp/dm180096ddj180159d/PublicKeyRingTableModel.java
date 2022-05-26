package etf.openpgp.dm180096ddj180159d;

import java.io.IOException;
import java.util.ArrayList;
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

	public void addKeyRing(PGPPublicKeyRing pkr) {

	}

	public PGPPublicKeyRingCollection getKeyRingCollection() throws IOException, PGPException {
		return new PGPPublicKeyRingCollection(publicKeyRingList);
	}

//	public PGPPublicKeyRingCollection getPublicKeyRingCollection() throws IOException, PGPException {
//	List<PGPPublicKeyRing> publicKeyRingCollection = new ArrayList<>();
//
//	for (PGPSecretKeyRing skr : this.keyRingCollection) {
//		Iterable<PGPPublicKey> iterable = () -> skr.getPublicKeys();
//		List<PGPPublicKey> publicKeys = StreamSupport.stream(iterable.spliterator(), false)
//				.collect(Collectors.toList());
//
//		PGPPublicKeyRing pkr = new PGPPublicKeyRing(publicKeys);
//
//		publicKeyRingCollection.add(pkr);
//	}
//
//	return new PGPPublicKeyRingCollection(publicKeyRingCollection);
//}

	public void setKeyRingList(PGPPublicKeyRingCollection keyRingCollection) {

		List<PGPPublicKeyRing> keyRingList = new ArrayList<>();
		Iterator<PGPPublicKeyRing> it = keyRingCollection.getKeyRings();
		while (it.hasNext()) {
			keyRingList.add(it.next());
		}

		setRowCount(0);
		for (PGPPublicKeyRing pkr : keyRingList) {
			addKeyRing(pkr);
		}
	}

}
