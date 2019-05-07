/* Reads the transform attribute as specified in
     * http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
private void readTransformAttribute(IXMLElement elem, HashMap<AttributeKey, Object> a) throws IOException {
    String value;
    value = readAttribute(elem, "transform", "none");
    if (!value.equals("none")) {
        TRANSFORM.put(a, toTransform(elem, value));
    }
}
