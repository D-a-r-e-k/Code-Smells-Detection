/**
	 * Gets an instance of an Image.
	 *
	 * @param filename
	 *            a filename
	 * @return an object of type <CODE>Gif</CODE>,<CODE>Jpeg</CODE> or
	 *         <CODE>Png</CODE>
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
public static Image getInstance(String filename) throws BadElementException, MalformedURLException, IOException {
    return getInstance(Utilities.toURL(filename));
}
