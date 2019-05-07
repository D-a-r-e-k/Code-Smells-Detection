/**
     * Reads an ODG "draw:page" element.
     */
private void readPageElement(IXMLElement elem) throws IOException {
    /* 9.1.4Drawing Pages
         *
        The element <draw:page> is a container for content in a drawing or presentation document.
        Drawing pages are used for the following:
        • Forms (see section 11.1)
        • Drawings (see section 9.2)
        • Frames (see section 9.3)
        • Presentation Animations (see section 9.7)
        • Presentation Notes (see section 9.1.5)
         *
        A master page must be assigned to each drawing page.
         *
        <define name="draw-page">
        <element name="draw:page">
        <ref name="common-presentation-header-footer-attlist"/>
        <ref name="draw-page-attlist"/>
        <optional>
        <ref name="office-forms"/>
        </optional>
        <zeroOrMore>
        <ref name="shape"/>
        </zeroOrMore>
        <optional>
        <choice>
        <ref name="presentation-animations"/>
        <ref name="animation-element"/>
        </choice>
        </optional>
        <optional>
        <ref name="presentation-notes"/>
        </optional>
        </element>
        </define>
         *
        The attributes that may be associated with the <draw:page> element are:
        • Page name
        • Page style
        • Master page
        • Presentation page layout
        • Header declaration
        • Footer declaration
        • Date and time declaration
        • ID
         *
        The elements that my be included in the <draw:page> element are:
        • Forms
        • Shapes
        • Animations
        • Presentation notes
         */
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            ODGFigure figure = readElement(child);
            if (figure != null) {
                figures.add(figure);
            }
        }
    }
}
