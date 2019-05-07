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
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 */
public static Image getInstance(int width, int height, int components, int bpc, byte data[]) throws BadElementException {
    return Image.getInstance(width, height, components, bpc, data, null);
}
