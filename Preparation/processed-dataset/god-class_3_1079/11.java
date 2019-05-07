/**
	 * Gets an instance of an Image from a java.awt.Image.
	 *
	 * @param image
	 *            the <CODE>java.awt.Image</CODE> to convert
	 * @param color
	 *            if different from <CODE>null</CODE> the transparency pixels
	 *            are replaced by this color
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 * @throws IOException
	 *             on error
	 */
public static Image getInstance(java.awt.Image image, java.awt.Color color) throws BadElementException, IOException {
    return Image.getInstance(image, color, false);
}
