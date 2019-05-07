/**
     * Reads an ODG "office:drawing" element.
     */
private void readDrawingElement(IXMLElement elem) throws IOException {
    /*
        2.3.2Drawing Documents
        The content of drawing document consists of a sequence of draw pages.
        <define name="office-body-content" combine="choice">
        <element name="office:drawing">
        <ref name="office-drawing-attlist"/>
        <ref name="office-drawing-content-prelude"/>
        <ref name="office-drawing-content-main"/>
        <ref name="office-drawing-content-epilogue"/>
        </element>
        </define>
        <define name="office-drawing-attlist">
        <empty/>
        </define>

        Drawing Document Content Model
        The drawing document prelude may contain text declarations only. To allow office applications to
        implement functionality that usually is available in spreadsheets for drawing documents, it may
        also contain elements that implement enhanced table features. See also section 2.3.4.
        <define name="office-drawing-content-prelude">
        <ref name="text-decls"/>
        <ref name="table-decls"/>
        </define>

        The main document content contains a sequence of draw pages.
        <define name="office-drawing-content-main">
        <zeroOrMore>
        <ref name="draw-page"/>
        </zeroOrMore>
        </define>

        There are no drawing documents specific epilogue elements, but the epilogue may contain
        elements that implement enhanced table features. See also section 2.3.4.
        <define name="office-drawing-content-epilogue">
        <ref name="table-functions"/>
        </define>
         */
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            if (child.getNamespace() == null || child.getNamespace().equals(DRAWING_NAMESPACE)) {
                String name = child.getName();
                if (name.equals("page")) {
                    readPageElement(child);
                }
            }
        }
    }
}
