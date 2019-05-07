/** Generates the font descriptor for this font.
     * @return the PdfDictionary containing the font descriptor or <CODE>null</CODE>
     * @param subsetPrefix the subset prefix
     * @param fontStream the indirect reference to a PdfStream containing the font or <CODE>null</CODE>
     */
protected PdfDictionary getFontDescriptor(PdfIndirectReference fontStream, String subsetPrefix, PdfIndirectReference cidset) {
    PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
    dic.put(PdfName.ASCENT, new PdfNumber(os_2.sTypoAscender * 1000 / head.unitsPerEm));
    dic.put(PdfName.CAPHEIGHT, new PdfNumber(os_2.sCapHeight * 1000 / head.unitsPerEm));
    dic.put(PdfName.DESCENT, new PdfNumber(os_2.sTypoDescender * 1000 / head.unitsPerEm));
    dic.put(PdfName.FONTBBOX, new PdfRectangle(head.xMin * 1000 / head.unitsPerEm, head.yMin * 1000 / head.unitsPerEm, head.xMax * 1000 / head.unitsPerEm, head.yMax * 1000 / head.unitsPerEm));
    if (cidset != null)
        dic.put(PdfName.CIDSET, cidset);
    if (cff) {
        if (encoding.startsWith("Identity-"))
            dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + fontName + "-" + encoding));
        else
            dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + fontName + style));
    } else
        dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + fontName + style));
    dic.put(PdfName.ITALICANGLE, new PdfNumber(italicAngle));
    dic.put(PdfName.STEMV, new PdfNumber(80));
    if (fontStream != null) {
        if (cff)
            dic.put(PdfName.FONTFILE3, fontStream);
        else
            dic.put(PdfName.FONTFILE2, fontStream);
    }
    int flags = 0;
    if (isFixedPitch)
        flags |= 1;
    flags |= fontSpecific ? 4 : 32;
    if ((head.macStyle & 2) != 0)
        flags |= 64;
    if ((head.macStyle & 1) != 0)
        flags |= 262144;
    dic.put(PdfName.FLAGS, new PdfNumber(flags));
    return dic;
}
