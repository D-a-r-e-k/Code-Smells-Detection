/** Generates the font dictionary for this font.
     * @return the PdfDictionary containing the font dictionary
     * @param subsetPrefix the subset prefix
     * @param firstChar the first valid character
     * @param lastChar the last valid character
     * @param shortTag a 256 bytes long <CODE>byte</CODE> array where each unused byte is represented by 0
     * @param fontDescriptor the indirect reference to a PdfDictionary containing the font descriptor or <CODE>null</CODE>
     */
protected PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, String subsetPrefix, int firstChar, int lastChar, byte shortTag[]) {
    PdfDictionary dic = new PdfDictionary(PdfName.FONT);
    if (cff) {
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        dic.put(PdfName.BASEFONT, new PdfName(fontName + style));
    } else {
        dic.put(PdfName.SUBTYPE, PdfName.TRUETYPE);
        dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + fontName + style));
    }
    dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + fontName + style));
    if (!fontSpecific) {
        for (int k = firstChar; k <= lastChar; ++k) {
            if (!differences[k].equals(notdef)) {
                firstChar = k;
                break;
            }
        }
        if (encoding.equals("Cp1252") || encoding.equals("MacRoman"))
            dic.put(PdfName.ENCODING, encoding.equals("Cp1252") ? PdfName.WIN_ANSI_ENCODING : PdfName.MAC_ROMAN_ENCODING);
        else {
            PdfDictionary enc = new PdfDictionary(PdfName.ENCODING);
            PdfArray dif = new PdfArray();
            boolean gap = true;
            for (int k = firstChar; k <= lastChar; ++k) {
                if (shortTag[k] != 0) {
                    if (gap) {
                        dif.add(new PdfNumber(k));
                        gap = false;
                    }
                    dif.add(new PdfName(differences[k]));
                } else
                    gap = true;
            }
            enc.put(PdfName.DIFFERENCES, dif);
            dic.put(PdfName.ENCODING, enc);
        }
    }
    dic.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
    dic.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
    PdfArray wd = new PdfArray();
    for (int k = firstChar; k <= lastChar; ++k) {
        if (shortTag[k] == 0)
            wd.add(new PdfNumber(0));
        else
            wd.add(new PdfNumber(widths[k]));
    }
    dic.put(PdfName.WIDTHS, wd);
    if (fontDescriptor != null)
        dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
    return dic;
}
