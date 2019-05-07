//	[L1] DocListener interface 
/**
     * Opens the document.
     * <P>
     * You have to open the document before you can begin to add content
     * to the body of the document.
     */
@Override
public void open() {
    if (!open) {
        super.open();
        writer.open();
        rootOutline = new PdfOutline(writer);
        currentOutline = rootOutline;
    }
    try {
        initPage();
    } catch (DocumentException de) {
        throw new ExceptionConverter(de);
    }
}
