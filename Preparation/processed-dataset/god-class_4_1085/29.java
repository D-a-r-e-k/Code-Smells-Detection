/**
     * Returns a PdfStream object with the full font program.
     * @return	a PdfStream with the font program
     * @since	2.1.3
     */
@Override
public PdfStream getFullFontStream() throws IOException, DocumentException {
    if (cff) {
        return new StreamFont(readCffFont(), "Type1C", compressionLevel);
    } else {
        byte[] b = getFullFont();
        int lengths[] = new int[] { b.length };
        return new StreamFont(b, lengths, compressionLevel);
    }
}
