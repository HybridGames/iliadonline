package com.iliadonline.shared.network;

import java.nio.ByteBuffer;

/**
 * ByteConverter is designed to work with data at a bitwise level.  Bytes in java cannot be unsigned, so we lose 128 possible values when only positives are valid.<br />
 * This allows us to break these positive only values into smaller byte arrays (2 bytes for UNSIGNED Short vs. 4 bytes for a standard Integer).<br />
 * @version 1.2 - Added Long and Double conversions
 * @version 1.3 - Using ByteBuffer as a conversion tool for long
 */
public class ByteConverter {
	
	public static final int UNSIGNED_SHORT_MAX_SIZE = 65536;
	public static final int UNSIGNED_BYTE_MAX_SIZE = 255;
	
	private static ByteBuffer bBuffer;
	
	/**
	 * Static Initializer
	 */
	static {
		//Allocating a buffer of 8 to handle LONGs as the bitwise conversion is failing so far.
		bBuffer = ByteBuffer.allocate(8);
		bBuffer.clear();
	}
	
	/**
	 * Performs bitwise byte to int conversions, the bits of the byte matter, not the literal value.
	 * @param value - The byte of information that is to be translated to a literal integer value
	 * @return - int - An integer who's value is determined by the order of the bits in the byte, not the byte's literal value
	 */
	public static int ByteToInt(final byte value)
	{
		//This pattern creates an integer and bitwise And's it against the byte producing an integer with the same low order bit pattern as the byte 
		return (value & 0x000000FF);
	}
	
	/**
	 * Performs a bitwise int to byte conversion.  All data besides the lowest order byte is dropped in the conversion.
	 * @param value - The int to be converted
	 * @return byte - A byte who's data is that of the lowest order byte of the integer value passed
	 * @throws IllegalArgumentException - Thrown if Value is > 255 or < 0
	 */
	public static byte IntToByte(final int value) throws IllegalArgumentException
	{
		if(value > UNSIGNED_BYTE_MAX_SIZE || value < 0)
		{
			throw new IllegalArgumentException("Integer value must be between 0 and 255");
		}
		
		//Type conversion drops higher order bits, keeping only the last byte of the integer
		//The bytes literal value will not be the same as the integer for values 128 - 255, but the bit order will be maintained
		return (byte)value;
	}
	
	/**
	 * Converts an Integer value to an array of 4 bytes.
	 * @param value - int to convert
	 * @return byte[] - Resulting array
	 */
	public static byte[] IntToByteArray(final int value)
	{
		byte[] result = new byte[4];
		
		result[0] = (byte)(value >> 24);
		result[1] = (byte)(value >> 16);
		result[2] = (byte)(value >> 8);
		result[3] = (byte)value;
		
		return result;
	}
	
	/**
	 * Converts an array of bytes to an Integer value.<br />
	 * Only converts the first 4 bytes in the array.
	 * @param array
	 * @return int
	 */
	public static int ByteArrayToInt(final byte[] array)
	{
		int result = 0;
		
		//The byte must be converted to an int through means of a bitwise And
		//this ensures a conversion based on bit patterns and not literal values
		result += (array[0] & 0x000000FF) << 24;
		result += (array[1] & 0x000000FF) << 16;
		result += (array[2] & 0x000000FF) << 8;
		result += (array[3] & 0x000000FF);
		
		return result;
	}
	
	/**
	 * Converts an Integer value to an array of 4 bytes.
	 * @param value - long to convert
	 * @return byte[] - Resulting array
	 */
	public static byte[] LongToByteArray(final long value)
	{
		byte[] result = new byte[8];
		result[0] = (byte)(value >> 56);
		result[1] = (byte)(value >> 48);
		result[2] = (byte)(value >> 40);
		result[3] = (byte)(value >> 32);
		result[4] = (byte)(value >> 24);
		result[5] = (byte)(value >> 16);
		result[6] = (byte)(value >> 8);
		result[7] = (byte)value;
		
		return result;
	}
	
	/**
	 * Converts an array of bytes to an Integer value.<br />
	 * Only converts the first 4 bytes in the array.
	 * @param array
	 * @return long
	 */
	public static long ByteArrayToLong(final byte[] array)
	{
		long result = 0;
		
		//TODO: Fix this so we don't have to allocate a buffer.
		/* Non-Working, replaced temporarily with ByteBuffer
		//The byte must be converted to a long through means of a bitwise And
		//this ensures a conversion based on bit patterns and not literal values
		result += (array[0] & 0x00000000000000FF) << 56;
		result += (array[1] & 0x00000000000000FF) << 48;
		result += (array[2] & 0x00000000000000FF) << 40;
		result += (array[3] & 0x00000000000000FF) << 32;
		result += (array[4] & 0x00000000000000FF) << 24;
		result += (array[5] & 0x00000000000000FF) << 16;
		result += (array[6] & 0x00000000000000FF) << 8;
		result += (array[7] & 0x00000000000000FF);
		*/
		
		bBuffer.put(array);
		bBuffer.flip();
		result = bBuffer.asLongBuffer().get();
		bBuffer.clear();
		return result;
	}
	
	/**
	 * Converts a Double to an array of byte[8]
	 * @param value - The double to convert
	 * @return
	 */
	public static byte[] DoubleToByteArray(final double value)
	{
		long bits = Double.doubleToLongBits(value);
		return LongToByteArray(bits);
	}
	
	/**
	 * Converts an array of byte[8] to a double
	 * @param array
	 * @return double - The double value held in the array
	 */
	public static double ByteArrayToDouble(final byte[] array)
	{
		long bits = ByteArrayToLong(array);
		return Double.longBitsToDouble(bits);
	}
	
	/**
	 * Converts a Short into a Byte Array.
	 * @param value - Short to convert
	 * @return byte[]
	 */
	public static byte[] ShortToByteArray(final short value)
	{
		byte[] result = new byte[2];
		
		result[0] = (byte)(value >> 8);
		result[1] = (byte)(value >> 0);
		
		return result;
	}
	
	/**
	 * Converts an UNSIGNED Short (represented by an Integer) to a Byte Array.<br />
	 * Allows us to use full 65k available values of an UNSIGNED Short, which only fits into an integer value in java
	 * @param value - Integer with a valid UNSIGNED Short value
	 * @return byte[]
	 */
	public static byte[] UnsignedShortToByteArray(final int value) throws IllegalArgumentException
	{
		if(value > (UNSIGNED_SHORT_MAX_SIZE) || value < 0)
		{
			throw (new IllegalArgumentException("0 <= Value <= " + UNSIGNED_SHORT_MAX_SIZE));
		}
		
		byte[] result = new byte[2];
		
		result[0] = (byte)(value >> 8);
		result[1] = (byte)(value >> 0);
		
		return result;
	}
	
	/**
	 * Converts a Byte Array to a Short
	 * @param array
	 * @return
	 */
	public static short ByteArrayToShort(final byte[] array)
	{
		short result = 0;
		
		//The byte must be converted to an int through means of a bitwise And
		//this ensures a conversion based on bit patterns and not literal values
		result += (array[0] & 0x000000FF) << 8;
		result += (array[1] & 0x000000FF);
		
		return result;
	}
	
	/**
	 * Converts Byte Array into an unsigned Short (represented by an Integer).<br />
	 * Allows us to use full 65k available values of an UNSIGNED Short, which only fits into an integer value
	 * @param byte[]
	 * @return  value - Integer with a valid UNSIGNED Short value 
	 */
	public static int ByteArrayToUnsignedShort(final byte[] array)
	{
		int result = 0;
		
		//The byte must be converted to an int through means of a bitwise And
		//this ensures a conversion based on bit patterns and not literal values
		result += (array[0] & 0x000000FF) << 8;
		result += (array[1] & 0x000000FF);
		
		return result;
	}
	
	/**
	 * Main function designed as an entry point to run tests on this class
	 * @param args
	 */
	public static void main(String... args)
	{
		
		//Tests all possible Long values
		//Because Double conversion relies on turning a double into a Long before byte conversion this implies Double to Byte Array works as well
		long l;
		long ltest;
		byte[] bytes;
		
		for(l = Long.MIN_VALUE; l <= Long.MAX_VALUE; l++)
		{
			bytes = LongToByteArray(l);
			ltest = ByteArrayToLong(bytes);
			
			if(ltest != l)
			{
				System.out.println("Failed Long Test");
				break;
			}
		}
		
		System.out.println("Long Test Complete");
	}
}
