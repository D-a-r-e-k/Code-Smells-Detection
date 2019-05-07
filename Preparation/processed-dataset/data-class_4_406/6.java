/**
	 * Writes out the slideshow file the is represented by an instance of this
	 * class
	 *
	 * @param out
	 *            The OutputStream to write to.
	 * @throws IOException
	 *             If there is an unexpected IOException from the passed in
	 *             OutputStream
	 */
public void write(OutputStream out) throws IOException {
    _hslfSlideShow.write(out);
}
