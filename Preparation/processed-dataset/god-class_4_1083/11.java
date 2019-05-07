PageStamp getPageStamp(int pageNum) {
    PdfDictionary pageN = reader.getPageN(pageNum);
    PageStamp ps = pagesToContent.get(pageN);
    if (ps == null) {
        ps = new PageStamp(this, reader, pageN);
        pagesToContent.put(pageN, ps);
    }
    return ps;
}
