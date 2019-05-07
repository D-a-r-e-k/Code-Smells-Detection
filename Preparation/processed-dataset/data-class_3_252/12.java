/**
	 * Gets an instance of a Image from a java.awt.Image.
	 * The image is added as a JPEG with a user defined quality.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> object to which the image will be added
	 * @param awtImage
	 *            the <CODE>java.awt.Image</CODE> to convert
	 * @param quality
	 *            a float value between 0 and 1
	 * @return an object of type <CODE>PdfTemplate</CODE>
	 * @throws BadElementException
	 *             on error
	 * @throws IOException
	 */
public static Image getInstance(PdfWriter writer, java.awt.Image awtImage, float quality) throws BadElementException, IOException {
    return getInstance(new PdfContentByte(writer), awtImage, quality);
}
