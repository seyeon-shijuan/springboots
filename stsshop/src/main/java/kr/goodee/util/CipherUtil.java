package kr.goodee.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
	private final static byte[] iv = new byte[] { 
			(byte) 0x8E, 0x12, 0x39,(byte) 0x9C,
			      0x07, 0x72, 0x6F, (byte) 0x5A,
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 
			      0x07, 0x72, 0x6F, (byte) 0x5A };
	static Cipher cipher;
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public static String makehash(String plain) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] pbyte = plain.getBytes();
		byte[] hash = md.digest(pbyte);
		return byteToHex(hash);
	}
	private static String byteToHex(byte[] hash) {
		if(hash == null) return null;
		String str = "";
		for(byte b : hash) str += String.format("%02X", b);
		return str;
	}
	public static String decrypt(String ciphermsg, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genkey = new SecretKeySpec(key.getBytes(), "AES");
			//초기화 벡터 : CBC모드에서 필요.
			AlgorithmParameterSpec paramSpec =
					                new IvParameterSpec(iv);
			//복호화 모드 설정
			cipher.init(Cipher.DECRYPT_MODE, genkey, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(ciphermsg.trim()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	private static byte[] hexToByte(String str) {
		if (str == null || str.length() < 2) return null;
		int len = str.length() / 2;
		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			buf[i] = (byte) Integer.parseInt
					(str.substring(i * 2, i * 2 + 2), 16);
		}
		return buf;
	}
	public static String encrypt(String plainMsg, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			Key genkey = new SecretKeySpec(key.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec =
					                new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, genkey, paramSpec);
			cipherMsg = cipher.doFinal(plainMsg.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
}
