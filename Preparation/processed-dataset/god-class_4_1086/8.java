/** Gets the page size, taking rotation into account. This
     * is a <CODE>Rectangle</CODE> with the value of the /MediaBox and the /Rotate key.
     * @param index the page number. The first page is 1
     * @return a <CODE>Rectangle</CODE>
     */
public Rectangle getPageSizeWithRotation(int index) {
    return getPageSizeWithRotation(pageRefs.getPageNRelease(index));
}
