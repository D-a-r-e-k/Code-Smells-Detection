/**
	 * Gets an instance of an Image in raw mode.
	 *
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param components
	 *            1,3 or 4 for GrayScale, RGB and CMYK
	 * @param data
	 *            the image data
	 * @param bpc
	 *            bits per component
	 * @param transparency
	 *            transparency information in the Mask format of the image
	 *            dictionary
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 */
public static Image getInstance(int width, int height, int components, int bpc, byte data[], int transparency[]) throws BadElementException {
    if (transparency != null && transparency.length != components * 2)
        throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.componentes.2"));
    if (components == 1 && bpc == 1) {
        byte g4[] = CCITTG4Encoder.compress(data, width, height);
        return Image.getInstance(width, height, false, Image.CCITTG4, Image.CCITT_BLACKIS1, g4, transparency);
    }
    Image img = new ImgRaw(width, height, components, bpc, data);
    img.transparency = transparency;
    return img;
}
