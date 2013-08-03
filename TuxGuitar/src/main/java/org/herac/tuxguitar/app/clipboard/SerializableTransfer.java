package org.herac.tuxguitar.app.clipboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;


public abstract class SerializableTransfer extends ByteArrayTransfer {

	@Override
	public void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof Serializable)) {
			// FIXME it should exists a dedicated exception
			System.err.println("Unsupported transfer type");
			return;
		}

		if (!isSupportedType(transferData)) {
			// FIXME it should exists a dedicated exception
			System.err.println("Unsupported transfer type");
			return;
		}

		Serializable serializable = (Serializable) object;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(out);
			objectStream.writeObject(serializable);
			objectStream.close();

			byte[] buffer = out.toByteArray();
			super.javaToNative(buffer, transferData);

		} catch (IOException e) {
			// FIXME it should exists a dedicated exception
			e.printStackTrace();
		}
	}

	@Override
	public Object nativeToJava(TransferData transferData) {

		if (!isSupportedType(transferData)) {
			// FIXME it should exists a dedicated exception
			System.err.println("Unsupported transfer type");
			return null;
		}

		byte[] buffer = (byte[]) super.nativeToJava(transferData);
		if (buffer == null) {
			// FIXME it should exists a dedicated exception
			System.err.println("Byte array expected");
			return null;
		}

		Serializable result = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = new ObjectInputStream(in);
			result = (Serializable) ois.readObject();
			ois.close();
		} catch (IOException e) {
			// FIXME it should exists a dedicated exception
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// FIXME it should exists a dedicated exception
			e.printStackTrace();
			return null;
		}
		return result;
	}
}