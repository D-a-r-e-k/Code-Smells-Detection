/** Character data in content. */
private void charDataInContent() {
    if (DEBUG_ELEMENT_CHILDREN) {
        System.out.println("charDataInContent()");
    }
    if (fElementChildren.length <= fElementChildrenLength) {
        QName[] newarray = new QName[fElementChildren.length * 2];
        System.arraycopy(fElementChildren, 0, newarray, 0, fElementChildren.length);
        fElementChildren = newarray;
    }
    QName qname = fElementChildren[fElementChildrenLength];
    if (qname == null) {
        for (int i = fElementChildrenLength; i < fElementChildren.length; i++) {
            fElementChildren[i] = new QName();
        }
        qname = fElementChildren[fElementChildrenLength];
    }
    qname.clear();
    fElementChildrenLength++;
}
