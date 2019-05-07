private QName createQName(String tagName) {
    tagName = modifyName(tagName, fNamesElems);
    return new QName(null, tagName, tagName, NamespaceBinder.XHTML_1_0_URI);
}
