/**
     * @param in
     * @param dicPar
     * @return a byte array
     */
public static byte[] decodePredictor(byte in[], PdfObject dicPar) {
    if (dicPar == null || !dicPar.isDictionary())
        return in;
    PdfDictionary dic = (PdfDictionary) dicPar;
    PdfObject obj = getPdfObject(dic.get(PdfName.PREDICTOR));
    if (obj == null || !obj.isNumber())
        return in;
    int predictor = ((PdfNumber) obj).intValue();
    if (predictor < 10)
        return in;
    int width = 1;
    obj = getPdfObject(dic.get(PdfName.COLUMNS));
    if (obj != null && obj.isNumber())
        width = ((PdfNumber) obj).intValue();
    int colors = 1;
    obj = getPdfObject(dic.get(PdfName.COLORS));
    if (obj != null && obj.isNumber())
        colors = ((PdfNumber) obj).intValue();
    int bpc = 8;
    obj = getPdfObject(dic.get(PdfName.BITSPERCOMPONENT));
    if (obj != null && obj.isNumber())
        bpc = ((PdfNumber) obj).intValue();
    DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(in));
    ByteArrayOutputStream fout = new ByteArrayOutputStream(in.length);
    int bytesPerPixel = colors * bpc / 8;
    int bytesPerRow = (colors * width * bpc + 7) / 8;
    byte[] curr = new byte[bytesPerRow];
    byte[] prior = new byte[bytesPerRow];
    // Decode the (sub)image row-by-row 
    while (true) {
        // Read the filter type byte and a row of data 
        int filter = 0;
        try {
            filter = dataStream.read();
            if (filter < 0) {
                return fout.toByteArray();
            }
            dataStream.readFully(curr, 0, bytesPerRow);
        } catch (Exception e) {
            return fout.toByteArray();
        }
        switch(filter) {
            case 0:
                //PNG_FILTER_NONE 
                break;
            case 1:
                //PNG_FILTER_SUB 
                for (int i = bytesPerPixel; i < bytesPerRow; i++) {
                    curr[i] += curr[i - bytesPerPixel];
                }
                break;
            case 2:
                //PNG_FILTER_UP 
                for (int i = 0; i < bytesPerRow; i++) {
                    curr[i] += prior[i];
                }
                break;
            case 3:
                //PNG_FILTER_AVERAGE 
                for (int i = 0; i < bytesPerPixel; i++) {
                    curr[i] += prior[i] / 2;
                }
                for (int i = bytesPerPixel; i < bytesPerRow; i++) {
                    curr[i] += ((curr[i - bytesPerPixel] & 0xff) + (prior[i] & 0xff)) / 2;
                }
                break;
            case 4:
                //PNG_FILTER_PAETH 
                for (int i = 0; i < bytesPerPixel; i++) {
                    curr[i] += prior[i];
                }
                for (int i = bytesPerPixel; i < bytesPerRow; i++) {
                    int a = curr[i - bytesPerPixel] & 0xff;
                    int b = prior[i] & 0xff;
                    int c = prior[i - bytesPerPixel] & 0xff;
                    int p = a + b - c;
                    int pa = Math.abs(p - a);
                    int pb = Math.abs(p - b);
                    int pc = Math.abs(p - c);
                    int ret;
                    if (pa <= pb && pa <= pc) {
                        ret = a;
                    } else if (pb <= pc) {
                        ret = b;
                    } else {
                        ret = c;
                    }
                    curr[i] += (byte) ret;
                }
                break;
            default:
                // Error -- unknown filter type 
                throw new RuntimeException(MessageLocalization.getComposedMessage("png.filter.unknown"));
        }
        try {
            fout.write(curr);
        } catch (IOException ioe) {
        }
        // Swap curr and prior 
        byte[] tmp = prior;
        prior = curr;
        curr = tmp;
    }
}
