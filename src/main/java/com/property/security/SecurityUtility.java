package com.property.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import org.freehold.utility.stringutility.StringUtility;

public class SecurityUtility {

	public static Integer generateRandomNumber(Integer from, Integer to)
	{
		Random r = new Random();
		return r.nextInt(to-from) + from;
	}
	
	public static String hashPassword(String password, String salt, Integer iterations) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] workOn = StringUtility.stringToByte(password);;
		byte[] saltBytes = StringUtility.stringToByte(salt);
		for(int i = 0; i < iterations; i++)
		{
			workOn = StringUtility.mergeByteArray(saltBytes, workOn);			
			md.update(workOn);
			workOn = md.digest();
		}
		
		return StringUtility.bytetoString(workOn);
	}
	
	public static String generateGUID()
	{
		return UUID.randomUUID().toString();
	}
	
	public static String generateGUIDWithoutDashes()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return StringUtility.bytetoString(bytes);
    }
}
