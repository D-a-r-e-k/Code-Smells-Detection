/** Empty element. */
public void emptyElement(final QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
    startElement(element, attrs, augs);
    // browser ignore the closing indication for non empty tags like <form .../> but not for unknown element 
    final HTMLElements.Element elem = getElement(element);
    if (elem.isEmpty() || elem.code == HTMLElements.UNKNOWN) {
        endElement(element, augs);
    }
}
