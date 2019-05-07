int getPageRotation(PdfDictionary page) {
    PdfNumber rotate = page.getAsNumber(PdfName.ROTATE);
    if (rotate == null)
        return 0;
    else {
        int n = rotate.intValue();
        n %= 360;
        return n < 0 ? n + 360 : n;
    }
}
