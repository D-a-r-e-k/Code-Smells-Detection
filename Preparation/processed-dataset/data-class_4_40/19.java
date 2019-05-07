// startElement(QName,XMLAttributes,Augmentations) 
/**
     * Forces an element start, taking care to set the information to allow startElement to "see" that's
     * the element has been forced.
     * @return <code>true</code> if creation could be done (TABLE's creation for instance can't be forced)
     */
private boolean forceStartElement(final QName elem, XMLAttributes attrs, final Augmentations augs) throws XNIException {
    forcedStartElement_ = true;
    startElement(elem, attrs, augs);
    return fElementStack.top > 0 && elem.equals(fElementStack.peek().qname);
}
