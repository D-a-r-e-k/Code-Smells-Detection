/**
	 * Creates an Image with CCITT G3 or G4 compression. It assumes that the
	 * data bytes are already compressed.
	 *
	 * @param width
	 *            the exact width of the image
	 * @param height
	 *            the exact height of the image
	 * @param reverseBits
	 *            reverses the bits in <code>data</code>. Bit 0 is swapped
	 *            with bit 7 and so on
	 * @param typeCCITT
	 *            the type of compression in <code>data</code>. It can be
	 *            CCITTG4, CCITTG31D, CCITTG32D
	 * @param parameters
	 *            parameters associated with this stream. Possible values are
	 *            CCITT_BLACKIS1, CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and
	 *            CCITT_ENDOFBLOCK or a combination of them
	 * @param data
	 *            the image data
	 * @return an Image object
	 * @throws BadElementException
	 *             on error
	 */
public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException {
    return Image.getInstance(width, height, reverseBits, typeCCITT, parameters, data, null);
}
