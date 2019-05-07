private void readCommonDrawingShapeAttributes(IXMLElement elem, HashMap<AttributeKey, Object> a) throws IOException {
    // The attribute draw:name assigns a name to the drawing shape. 
    NAME.put(a, elem.getAttribute("name", DRAWING_NAMESPACE, null));
    // The draw:transform attribute specifies a list of transformations that 
    // can be applied to a drawing shape. 
    TRANSFORM.put(a, toTransform(elem.getAttribute("transform", DRAWING_NAMESPACE, null)));
}
