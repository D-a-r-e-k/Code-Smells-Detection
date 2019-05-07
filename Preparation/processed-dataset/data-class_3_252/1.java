/**
	 * Gets an instance of an Image.
	 *
	 * @param url
	 *            an URL
	 * @return an Image
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
public static Image getInstance(URL url) throws BadElementException, MalformedURLException, IOException {
    InputStream is = null;
    try {
        is = url.openStream();
        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();
        int c4 = is.read();
        // jbig2 
        int c5 = is.read();
        int c6 = is.read();
        int c7 = is.read();
        int c8 = is.read();
        is.close();
        is = null;
        if (c1 == 'G' && c2 == 'I' && c3 == 'F') {
            GifImage gif = new GifImage(url);
            Image img = gif.getImage(1);
            return img;
        }
        if (c1 == 0xFF && c2 == 0xD8) {
            return new Jpeg(url);
        }
        if (c1 == 0x00 && c2 == 0x00 && c3 == 0x00 && c4 == 0x0c) {
            return new Jpeg2000(url);
        }
        if (c1 == 0xff && c2 == 0x4f && c3 == 0xff && c4 == 0x51) {
            return new Jpeg2000(url);
        }
        if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
            return PngImage.getImage(url);
        }
        if (c1 == 0xD7 && c2 == 0xCD) {
            return new ImgWMF(url);
        }
        if (c1 == 'B' && c2 == 'M') {
            return BmpImage.getImage(url);
        }
        if (c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42 || c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0) {
            RandomAccessFileOrArray ra = null;
            try {
                if (url.getProtocol().equals("file")) {
                    String file = url.getFile();
                    file = Utilities.unEscapeURL(file);
                    ra = new RandomAccessFileOrArray(file);
                } else
                    ra = new RandomAccessFileOrArray(url);
                Image img = TiffImage.getTiffImage(ra, 1);
                img.url = url;
                return img;
            } finally {
                if (ra != null)
                    ra.close();
            }
        }
        if (c1 == 0x97 && c2 == 'J' && c3 == 'B' && c4 == '2' && c5 == '\r' && c6 == '\n' && c7 == 0x1a && c8 == '\n') {
            RandomAccessFileOrArray ra = null;
            try {
                if (url.getProtocol().equals("file")) {
                    String file = url.getFile();
                    file = Utilities.unEscapeURL(file);
                    ra = new RandomAccessFileOrArray(file);
                } else
                    ra = new RandomAccessFileOrArray(url);
                Image img = JBIG2Image.getJbig2Image(ra, 1);
                img.url = url;
                return img;
            } finally {
                if (ra != null)
                    ra.close();
            }
        }
        throw new IOException(url.toString() + " is not a recognized imageformat.");
    } finally {
        if (is != null) {
            is.close();
        }
    }
}
