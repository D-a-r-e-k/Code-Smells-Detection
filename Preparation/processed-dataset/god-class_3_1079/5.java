/**
	 * Creates a JBIG2 Image.
	 * @param	width	the width of the image
	 * @param	height	the height of the image
	 * @param	data	the raw image data
	 * @param	globals	JBIG2 globals
	 * @since	2.1.5
	 */
public static Image getInstance(int width, int height, byte[] data, byte[] globals) {
    Image img = new ImgJBIG2(width, height, data, globals);
    return img;
}
