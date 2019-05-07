/** Gets the page size without taking rotation into account. This
     * is the value of the /MediaBox key.
     * @param index the page number. The first page is 1
     * @return the page size
     */
public Rectangle getPageSize(int index) {
    return getPageSize(pageRefs.getPageNRelease(index));
}
