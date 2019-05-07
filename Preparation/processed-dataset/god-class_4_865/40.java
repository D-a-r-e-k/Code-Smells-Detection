// getElementDepth(HTMLElements.Element) 
/**
     * Returns the depth of the open tag associated with the specified
     * element parent names or -1 if no matching element is found.
     *
     * @param parents The parent elements.
     */
protected int getParentDepth(HTMLElements.Element[] parents, short bounds) {
    if (parents != null) {
        for (int i = fElementStack.top - 1; i >= 0; i--) {
            Info info = fElementStack.data[i];
            if (info.element.code == bounds) {
                break;
            }
            for (int j = 0; j < parents.length; j++) {
                if (info.element.code == parents[j].code) {
                    return fElementStack.top - i;
                }
            }
        }
    }
    return -1;
}
