//	[L2] DocListener interface 
/**
     * Closes the document.
     * <B>
     * Once all the content has been written in the body, you have to close
     * the body. After that nothing can be written to the body anymore.
     */
@Override
public void close() {
    if (close) {
        return;
    }
    try {
        boolean wasImage = imageWait != null;
        newPage();
        if (imageWait != null || wasImage)
            newPage();
        if (annotationsImp.hasUnusedAnnotations())
            throw new RuntimeException(MessageLocalization.getComposedMessage("not.all.annotations.could.be.added.to.the.document.the.document.doesn.t.have.enough.pages"));
        PdfPageEvent pageEvent = writer.getPageEvent();
        if (pageEvent != null)
            pageEvent.onCloseDocument(writer, this);
        super.close();
        writer.addLocalDestinations(localDestinations);
        calculateOutlineCount();
        writeOutlines();
    } catch (Exception e) {
        throw ExceptionConverter.convertException(e);
    }
    writer.close();
}
