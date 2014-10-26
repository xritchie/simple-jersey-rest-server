package org.freehold.utility.stringutility;

import org.apache.commons.codec.binary.Base64;

public class StringUtility {

	public static boolean hasValue(String str)
	{
		if ((str != null) && (str.length() > 0))
			return true;
		
		return false;
	}
	
	public static String removeNoneAlphaNumeric(String value)
	{
		return value.replaceAll("[^A-Za-z0-9]", "");
	}
	
	public static String removeWhiteSpaces(String value)
	{
		return value.replaceAll("\\s+","");
	}
	
	public static String bytetoString(byte[] input) {
        return Base64.encodeBase64String(input);
    }
	
	public static byte[] stringToByte(String input) {
		if (input == null)
			return null;
		
		if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);
        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }

	public static byte[] mergeByteArray(byte[] merge, byte[] with)
	{
		byte[] combined = new byte[merge.length + with.length];
		
		for (int i = 0; i < combined.length; ++i)
			combined[i] = i < merge.length ? merge[i] : with[i - merge.length];
			
		return combined;
	}
}
