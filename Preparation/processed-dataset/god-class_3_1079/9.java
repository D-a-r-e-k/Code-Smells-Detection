// images from a PdfTemplate 
/**
	 * gets an instance of an Image
	 *
	 * @param template
	 *            a PdfTemplate that has to be wrapped in an Image object
	 * @return an Image object
	 * @throws BadElementException
	 */
public static Image getInstance(PdfTemplate template) throws BadElementException {
    return new ImgTemplate(template);
}
