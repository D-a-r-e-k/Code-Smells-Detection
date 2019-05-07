PdfContentByte getUnderContent(int pageNum) {
    if (pageNum < 1 || pageNum > reader.getNumberOfPages())
        return null;
    PageStamp ps = getPageStamp(pageNum);
    if (ps.under == null)
        ps.under = new StampContent(this, ps);
    return ps.under;
}
