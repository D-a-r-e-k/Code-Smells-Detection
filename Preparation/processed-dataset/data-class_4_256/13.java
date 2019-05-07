PdfContentByte getOverContent(int pageNum) {
    if (pageNum < 1 || pageNum > reader.getNumberOfPages())
        return null;
    PageStamp ps = getPageStamp(pageNum);
    if (ps.over == null)
        ps.over = new StampContent(this, ps);
    return ps.over;
}
