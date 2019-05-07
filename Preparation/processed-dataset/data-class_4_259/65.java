/** Get the content from a stream applying the required filters.
     * @param stream the stream
     * @param file the location where the stream is
     * @throws IOException on error
     * @return the stream content
     */
public static byte[] getStreamBytes(PRStream stream, RandomAccessFileOrArray file) throws IOException {
    PdfObject filter = getPdfObjectRelease(stream.get(PdfName.FILTER));
    byte[] b = getStreamBytesRaw(stream, file);
    ArrayList<PdfObject> filters = new ArrayList<PdfObject>();
    if (filter != null) {
        if (filter.isName())
            filters.add(filter);
        else if (filter.isArray())
            filters = ((PdfArray) filter).getArrayList();
    }
    ArrayList<PdfObject> dp = new ArrayList<PdfObject>();
    PdfObject dpo = getPdfObjectRelease(stream.get(PdfName.DECODEPARMS));
    if (dpo == null || !dpo.isDictionary() && !dpo.isArray())
        dpo = getPdfObjectRelease(stream.get(PdfName.DP));
    if (dpo != null) {
        if (dpo.isDictionary())
            dp.add(dpo);
        else if (dpo.isArray())
            dp = ((PdfArray) dpo).getArrayList();
    }
    PdfName name;
    for (int j = 0; j < filters.size(); ++j) {
        name = (PdfName) getPdfObjectRelease(filters.get(j));
        if (PdfName.FLATEDECODE.equals(name) || PdfName.FL.equals(name)) {
            b = FlateDecode(b);
            PdfObject dicParam = null;
            if (j < dp.size()) {
                dicParam = dp.get(j);
                b = decodePredictor(b, dicParam);
            }
        } else if (PdfName.ASCIIHEXDECODE.equals(name) || PdfName.AHX.equals(name))
            b = ASCIIHexDecode(b);
        else if (PdfName.ASCII85DECODE.equals(name) || PdfName.A85.equals(name))
            b = ASCII85Decode(b);
        else if (PdfName.LZWDECODE.equals(name)) {
            b = LZWDecode(b);
            PdfObject dicParam = null;
            if (j < dp.size()) {
                dicParam = dp.get(j);
                b = decodePredictor(b, dicParam);
            }
        } else if (PdfName.CCITTFAXDECODE.equals(name)) {
            PdfNumber wn = (PdfNumber) getPdfObjectRelease(stream.get(PdfName.WIDTH));
            PdfNumber hn = (PdfNumber) getPdfObjectRelease(stream.get(PdfName.HEIGHT));
            if (wn == null || hn == null)
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("filter.ccittfaxdecode.is.only.supported.for.images"));
            int width = wn.intValue();
            int height = hn.intValue();
            PdfDictionary param = null;
            if (j < dp.size()) {
                PdfObject objParam = getPdfObjectRelease((PdfObject) dp.get(j));
                if (objParam != null && (objParam instanceof PdfDictionary))
                    param = (PdfDictionary) objParam;
            }
            int k = 0;
            boolean blackIs1 = false;
            boolean byteAlign = false;
            if (param != null) {
                PdfNumber kn = param.getAsNumber(PdfName.K);
                if (kn != null)
                    k = kn.intValue();
                PdfBoolean bo = param.getAsBoolean(PdfName.BLACKIS1);
                if (bo != null)
                    blackIs1 = bo.booleanValue();
                bo = param.getAsBoolean(PdfName.ENCODEDBYTEALIGN);
                if (bo != null)
                    byteAlign = bo.booleanValue();
            }
            byte[] outBuf = new byte[(width + 7) / 8 * height];
            TIFFFaxDecompressor decoder = new TIFFFaxDecompressor();
            if (k == 0 || k > 0) {
                int tiffT4Options = k > 0 ? TIFFConstants.GROUP3OPT_2DENCODING : 0;
                tiffT4Options |= byteAlign ? TIFFConstants.GROUP3OPT_FILLBITS : 0;
                decoder.SetOptions(1, TIFFConstants.COMPRESSION_CCITTFAX3, tiffT4Options, 0);
                decoder.decodeRaw(outBuf, b, width, height);
                if (decoder.fails > 0) {
                    byte[] outBuf2 = new byte[(width + 7) / 8 * height];
                    int oldFails = decoder.fails;
                    decoder.SetOptions(1, TIFFConstants.COMPRESSION_CCITTRLE, tiffT4Options, 0);
                    decoder.decodeRaw(outBuf2, b, width, height);
                    if (decoder.fails < oldFails) {
                        outBuf = outBuf2;
                    }
                }
            } else {
                decoder.SetOptions(1, TIFFConstants.COMPRESSION_CCITTFAX4, 0, 0);
                decoder.decodeRaw(outBuf, b, width, height);
            }
            if (!blackIs1) {
                int len = outBuf.length;
                for (int t = 0; t < len; ++t) {
                    outBuf[t] ^= 0xff;
                }
            }
            b = outBuf;
        } else if (PdfName.CRYPT.equals(name)) {
        } else
            throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.filter.1.is.not.supported", name));
    }
    return b;
}
