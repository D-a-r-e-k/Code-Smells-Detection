/** Outputs to the writer the font dictionaries and streams.
     * @param writer the writer for this document
     * @param ref the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException on error
     * @throws DocumentException error in generating the object
     */
@Override
void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException {
    int firstChar = ((Integer) params[0]).intValue();
    int lastChar = ((Integer) params[1]).intValue();
    byte shortTag[] = (byte[]) params[2];
    boolean subsetp = ((Boolean) params[3]).booleanValue() && subset;
    if (!subsetp) {
        firstChar = 0;
        lastChar = shortTag.length - 1;
        for (int k = 0; k < shortTag.length; ++k) shortTag[k] = 1;
    }
    PdfIndirectReference ind_font = null;
    PdfObject pobj = null;
    PdfIndirectObject obj = null;
    String subsetPrefix = "";
    if (embedded) {
        if (cff) {
            pobj = new StreamFont(readCffFont(), "Type1C", compressionLevel);
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        } else {
            if (subsetp)
                subsetPrefix = createSubsetPrefix();
            HashSet<Integer> glyphs = new HashSet<Integer>();
            for (int k = firstChar; k <= lastChar; ++k) {
                if (shortTag[k] != 0) {
                    int[] metrics = null;
                    if (specialMap != null) {
                        int[] cd = GlyphList.nameToUnicode(differences[k]);
                        if (cd != null)
                            metrics = getMetricsTT(cd[0]);
                    } else {
                        if (fontSpecific)
                            metrics = getMetricsTT(k);
                        else
                            metrics = getMetricsTT(unicodeDifferences[k]);
                    }
                    if (metrics != null)
                        glyphs.add(new Integer(metrics[0]));
                }
            }
            addRangeUni(glyphs, subsetp);
            byte[] b = null;
            if (subsetp || directoryOffset != 0 || subsetRanges != null) {
                TrueTypeFontSubSet sb = new TrueTypeFontSubSet(fileName, new RandomAccessFileOrArray(rf), glyphs, directoryOffset, true, !subsetp);
                b = sb.process();
            } else {
                b = getFullFont();
            }
            int lengths[] = new int[] { b.length };
            pobj = new StreamFont(b, lengths, compressionLevel);
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
    }
    pobj = getFontDescriptor(ind_font, subsetPrefix, null);
    if (pobj != null) {
        obj = writer.addToBody(pobj);
        ind_font = obj.getIndirectReference();
    }
    pobj = getFontBaseType(ind_font, subsetPrefix, firstChar, lastChar, shortTag);
    writer.addToBody(pobj, ref);
}
