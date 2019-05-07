/**
	 * Adds a picture to this presentation and returns the associated index.
	 *
	 * @param pict
	 *            the file containing the image to add
	 * @param format
	 *            the format of the picture. One of constans defined in the
	 *            <code>Picture</code> class.
	 * @return the index to this picture (1 based).
	 */
public int addPicture(File pict, int format) throws IOException {
    int length = (int) pict.length();
    byte[] data = new byte[length];
    try {
        FileInputStream is = new FileInputStream(pict);
        is.read(data);
        is.close();
    } catch (IOException e) {
        throw new HSLFException(e);
    }
    return addPicture(data, format);
}
