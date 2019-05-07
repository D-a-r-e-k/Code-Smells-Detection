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
	 * @param transparency
	 *            transparency information in the Mask format of the image
	 *            dictionary
	 * @return an Image object
	 * @throws BadElementException
	 *             on error
	 */
public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data, int transparency[]) throws BadElementException {
    if (transparency != null && transparency.length != 2)
        throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.2.with.ccitt.images"));
    Image img = new ImgCCITT(width, height, reverseBits, typeCCITT, parameters, data);
    img.transparency = transparency;
    return img;
}
