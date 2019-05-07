/**
     * Replaces CalRGB and CalGray colorspaces with DeviceRGB and DeviceGray.
     */
public void simplifyColorspace() {
    if (additional == null)
        return;
    PdfArray value = additional.getAsArray(PdfName.COLORSPACE);
    if (value == null)
        return;
    PdfObject cs = simplifyColorspace(value);
    PdfObject newValue;
    if (cs.isName())
        newValue = cs;
    else {
        newValue = value;
        PdfName first = value.getAsName(0);
        if (PdfName.INDEXED.equals(first)) {
            if (value.size() >= 2) {
                PdfArray second = value.getAsArray(1);
                if (second != null) {
                    value.set(1, simplifyColorspace(second));
                }
            }
        }
    }
    additional.put(PdfName.COLORSPACE, newValue);
}
