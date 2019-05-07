/**
     * Updates the count in the outlines.
     */
void calculateOutlineCount() {
    if (rootOutline.getKids().size() == 0)
        return;
    traverseOutlineCount(rootOutline);
}
