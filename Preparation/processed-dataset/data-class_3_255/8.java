/**
     * Adds an element. Elements supported are <CODE>Paragraph</CODE>,
     * <CODE>List</CODE>, <CODE>PdfPTable</CODE>, <CODE>Image</CODE> and
     * <CODE>Graphic</CODE>.
     * <p>
     * It removes all the text placed with <CODE>addText()</CODE>.
     *
     * @param element the <CODE>Element</CODE>
     */
public void addElement(Element element) {
    if (element == null)
        return;
    if (element instanceof Image) {
        Image img = (Image) element;
        PdfPTable t = new PdfPTable(1);
        float w = img.getWidthPercentage();
        if (w == 0) {
            t.setTotalWidth(img.getScaledWidth());
            t.setLockedWidth(true);
        } else
            t.setWidthPercentage(w);
        t.setSpacingAfter(img.getSpacingAfter());
        t.setSpacingBefore(img.getSpacingBefore());
        switch(img.getAlignment()) {
            case Image.LEFT:
                t.setHorizontalAlignment(Element.ALIGN_LEFT);
                break;
            case Image.RIGHT:
                t.setHorizontalAlignment(Element.ALIGN_RIGHT);
                break;
            default:
                t.setHorizontalAlignment(Element.ALIGN_CENTER);
                break;
        }
        PdfPCell c = new PdfPCell(img, true);
        c.setPadding(0);
        c.setBorder(img.getBorder());
        c.setBorderColor(img.getBorderColor());
        c.setBorderWidth(img.getBorderWidth());
        c.setBackgroundColor(img.getBackgroundColor());
        t.addCell(c);
        element = t;
    }
    if (element.type() == Element.CHUNK) {
        element = new Paragraph((Chunk) element);
    } else if (element.type() == Element.PHRASE) {
        element = new Paragraph((Phrase) element);
    }
    if (element.type() != Element.PARAGRAPH && element.type() != Element.LIST && element.type() != Element.PTABLE && element.type() != Element.YMARK)
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("element.not.allowed"));
    if (!composite) {
        composite = true;
        compositeElements = new LinkedList<Element>();
        bidiLine = null;
        waitPhrase = null;
    }
    compositeElements.add(element);
}
