package com.tscp.mvna.web.session.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface Encryptor {

	public String encryptUrlSafe(
			String input) throws UnsupportedEncodingException;

	public String decryptUrlSafe(
			String input) throws IOException;

	public String encryptIntUrlSafe(
			int input) throws UnsupportedEncodingException;

	public int decryptIntUrlSafe(
			String input) throws IOException, NumberFormatException;

}
