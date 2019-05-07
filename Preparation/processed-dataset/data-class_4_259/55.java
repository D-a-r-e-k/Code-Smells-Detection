/** Gets the dictionary that represents a page.
     * @param pageNum the page number. 1 is the first
     * @return the page dictionary
     */
public PdfDictionary getPageN(int pageNum) {
    PdfDictionary dic = pageRefs.getPageN(pageNum);
    if (dic == null)
        return null;
    if (appendable)
        dic.setIndRef(pageRefs.getPageOrigRef(pageNum));
    return dic;
}
