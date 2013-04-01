/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   Copyright (C) 2005 - Matteo Merli - matteo.merli@gmail.com            *
 *                                                                         *
 ***************************************************************************/

/*
 * $Id: UnsignedByte.java 315 2005-12-04 14:48:24Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/lib/number/UnsignedByte.java $
 * 
 */
package rtspproxy.lib.number;

/**
 * The UnsignedByte class wraps a value of and unsigned 8 bits number.
 * 
 * @author Matteo Merli
 */
public final class UnsignedByte extends UnsignedNumber {
	static final long serialVersionUID = 1L;

	private short value;

	public UnsignedByte(byte c) {
		value = c;
	}

	public UnsignedByte(short c) {
		value = (short) (c & 0xFF);
	}

	public UnsignedByte(int c) {
		value = (short) (c & 0xFF);
	}

	public UnsignedByte(long c) {
		value = (short) (c & 0xFFL);
	}

	private UnsignedByte() {
		value = 0;
	}

	public static UnsignedByte fromBytes(byte[] c) {
		return fromBytes(c, 0);
	}

	public static UnsignedByte fromBytes(byte[] c, int idx) {
		UnsignedByte number = new UnsignedByte();
		if ((c.length - idx) < 1)
			throw new IllegalArgumentException("An UnsignedByte number is composed of 1 byte.");

		number.value = (short) (c[0] & 0xFF);
		return number;
	}

	public static UnsignedByte fromString(String c) {
		return fromString(c, 10);
	}

	public static UnsignedByte fromString(String c, int radix) {
		UnsignedByte number = new UnsignedByte();

		short v = Short.parseShort(c, radix);
		number.value = (short) (v & 0xFF);
		return number;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public short shortValue() {
		return (short) (value & 0xFF);
	}

	@Override
	public int intValue() {
		return value & 0xFF;
	}

	@Override
	public long longValue() {
		return value & 0xFFL;
	}

	@Override
	public byte[] getBytes() {
		byte[] c = { (byte) (value & 0xFF) };
		return c;
	}

	@Override
	public int compareTo(UnsignedNumber other) {
		short otherValue = other.shortValue();
		if (value > otherValue)
			return +1;
		else if (value < otherValue)
			return -1;
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		return value == ((Number) other).shortValue();
	}

	@Override
	public int hashCode() {
		return (int) value;
	}

	@Override
	public String toString() {
		return Short.toString(value);
	}

	@Override
	public void shiftRight(int nBits) {
		if (Math.abs(nBits) > 8)
			throw new IllegalArgumentException("Cannot right shift " + nBits + " an UnsignedByte.");

		value >>>= nBits;
	}

	@Override
	public void shiftLeft(int nBits) {
		if (Math.abs(nBits) > 8)
			throw new IllegalArgumentException("Cannot left shift " + nBits + " an UnsignedByte.");

		value <<= nBits;
	}
}
