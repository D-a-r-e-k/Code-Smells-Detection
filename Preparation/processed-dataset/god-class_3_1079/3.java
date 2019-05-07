/**
	 * gets an instance of an Image
	 *
	 * @param imgb
	 *            raw image date
	 * @return an Image object
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
public static Image getInstance(byte imgb[]) throws BadElementException, MalformedURLException, IOException {
    InputStream is = null;
    try {
        is = new java.io.ByteArrayInputStream(imgb);
        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();
        int c4 = is.read();
        is.close();
        is = null;
        if (c1 == 'G' && c2 == 'I' && c3 == 'F') {
            GifImage gif = new GifImage(imgb);
            return gif.getImage(1);
        }
        if (c1 == 0xFF && c2 == 0xD8) {
            return new Jpeg(imgb);
        }
        if (c1 == 0x00 && c2 == 0x00 && c3 == 0x00 && c4 == 0x0c) {
            return new Jpeg2000(imgb);
        }
        if (c1 == 0xff && c2 == 0x4f && c3 == 0xff && c4 == 0x51) {
            return new Jpeg2000(imgb);
        }
        if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
            return PngImage.getImage(imgb);
        }
        if (c1 == 0xD7 && c2 == 0xCD) {
            return new ImgWMF(imgb);
        }
        if (c1 == 'B' && c2 == 'M') {
            return BmpImage.getImage(imgb);
        }
        if (c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42 || c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0) {
            RandomAccessFileOrArray ra = null;
            try {
                ra = new RandomAccessFileOrArray(imgb);
                Image img = TiffImage.getTiffImage(ra, 1);
                if (img.getOriginalData() == null)
                    img.setOriginalData(imgb);
                return img;
            } finally {
                if (ra != null)
                    ra.close();
            }
        }
        if (c1 == 0x97 && c2 == 'J' && c3 == 'B' && c4 == '2') {
            is = new java.io.ByteArrayInputStream(imgb);
            is.skip(4);
            int c5 = is.read();
            int c6 = is.read();
            int c7 = is.read();
            int c8 = is.read();
            if (c5 == '\r' && c6 == '\n' && c7 == 0x1a && c8 == '\n') {
                int file_header_flags = is.read();
                int number_of_pages = -1;
                if ((file_header_flags & 0x2) == 0x2) {
                    number_of_pages = is.read() << 24 | is.read() << 16 | is.read() << 8 | is.read();
                }
                is.close();
                // a jbig2 file with a file header.  the header is the only way we know here. 
                // embedded jbig2s don't have a header, have to create them by explicit use of Jbig2Image? 
                // nkerr, 2008-12-05  see also the getInstance(URL) 
                RandomAccessFileOrArray ra = null;
                try {
                    ra = new RandomAccessFileOrArray(imgb);
                    Image img = JBIG2Image.getJbig2Image(ra, 1);
                    if (img.getOriginalData() == null)
                        img.setOriginalData(imgb);
                    return img;
                } finally {
                    if (ra != null)
                        ra.close();
                }
            }
        }
        throw new IOException(MessageLocalization.getComposedMessage("the.byte.array.is.not.a.recognized.imageformat"));
    } finally {
        if (is != null) {
            is.close();
        }
    }
}
