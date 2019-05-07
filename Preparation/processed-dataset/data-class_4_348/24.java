/**
   *     5. [specified] A flag indicating whether this attribute was actually
   *        specified in the start-tag of its element, or was defaulted from the
   *        DTD.
   *
   * @param attributeHandle the attribute handle
   * @return <code>true</code> if the attribute was specified;
   *         <code>false</code> if it was defaulted.
   */
public boolean isAttributeSpecified(int attributeHandle) {
    int type = getNodeType(attributeHandle);
    if (DTM.ATTRIBUTE_NODE == type) {
        Attr attr = (Attr) getNode(attributeHandle);
        return attr.getSpecified();
    }
    return false;
}
