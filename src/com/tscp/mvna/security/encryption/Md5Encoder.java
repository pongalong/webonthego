package com.tscp.mvna.security.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

/**
 * MD5 encoder
 * 
 * @author User
 * 
 */
public final class Md5Encoder {
	private static MessageDigest md5;

	public static final String encode(String string) throws RuntimeException {
		return toHex(digestString(string, null));
	}

	protected static final String encode(String string, String encoding) throws RuntimeException {
		return toHex(digestString(string, encoding));
	}

	private static final String toHex(byte[] digest) {
		String hexString = new String(Hex.encode(digest));
		return hexString;
	}

	private static final byte[] digestString(String string, String encoding) throws RuntimeException {
		byte[] data;
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		try {
			data = string.getBytes(encoding);
		} catch (UnsupportedEncodingException x) {
			throw new RuntimeException(x.toString());
		}
		return digestBytes(data);
	}

	private static final byte[] digestBytes(byte[] data) throws RuntimeException {
		synchronized (Md5Encoder.class) {
			if (md5 == null) {
				try {
					md5 = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e.toString());
				}
			}
			return md5.digest(data);
		}
	}

	/**
	 * <p>
	 * Returns a string of astericks of the same length as the input.
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public static final String muddle(String data) {
		StringBuilder muddledData = new StringBuilder();
		for (int i = 0; i < data.length(); i++) {
			muddledData.append("*");
		}
		return muddledData.toString();
	}

}
