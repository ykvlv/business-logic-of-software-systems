package ykvlv.blss.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

	private static final String ALGORITHM = "SHA-256";

	public static String getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return bytesToHex(salt);
	}

	public static String hashPassword(String passwordToHash, String salt) {
		String generatedPassword;
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			generatedPassword = bytesToHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return generatedPassword;
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
