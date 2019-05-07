/**
     * @param pageNum
     * @return a Dictionary object
     */
public PdfDictionary getPageNRelease(int pageNum) {
    PdfDictionary dic = getPageN(pageNum);
    pageRefs.releasePage(pageNum);
    return dic;
}
