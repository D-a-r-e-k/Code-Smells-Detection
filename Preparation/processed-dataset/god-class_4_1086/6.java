/**
     * Gets the page rotation. This value can be 0, 90, 180 or 270.
     * @param index the page number. The first page is 1
     * @return the page rotation
     */
public int getPageRotation(int index) {
    return getPageRotation(pageRefs.getPageNRelease(index));
}
