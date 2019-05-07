// callEndElement(QName,Augmentations) 
/**
     * Returns the depth of the open tag associated with the specified
     * element name or -1 if no matching element is found.
     *
     * @param element The element.
     */
protected final int getElementDepth(HTMLElements.Element element) {
    final boolean container = element.isContainer();
    final short elementCode = element.code;
    final boolean tableBodyOrHtml = (elementCode == HTMLElements.TABLE) || (elementCode == HTMLElements.BODY) || (elementCode == HTMLElements.HTML);
    int depth = -1;
    for (int i = fElementStack.top - 1; i >= fragmentContextStackSize_; i--) {
        Info info = fElementStack.data[i];
        if (info.element.code == element.code) {
            depth = fElementStack.top - i;
            break;
        }
        if (!container && info.element.isBlock()) {
            break;
        }
        if (info.element.code == HTMLElements.TABLE && !tableBodyOrHtml) {
            return -1;
        }
    }
    return depth;
}
