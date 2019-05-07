/**
     * Makes a new page and sends it to the <CODE>PdfWriter</CODE>.
     *
     * @return a <CODE>boolean</CODE>
     */
@Override
public boolean newPage() {
    lastElementType = -1;
    if (isPageEmpty()) {
        setNewPageSizeAndMargins();
        return false;
    }
    if (!open || close) {
        throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
    }
    PdfPageEvent pageEvent = writer.getPageEvent();
    if (pageEvent != null)
        pageEvent.onEndPage(writer, this);
    //Added to inform any listeners that we are moving to a new page (added by David Freels) 
    super.newPage();
    // the following 2 lines were added by Pelikan Stephan 
    indentation.imageIndentLeft = 0;
    indentation.imageIndentRight = 0;
    try {
        // we flush the arraylist with recently written lines 
        flushLines();
        // we prepare the elements of the page dictionary 
        // [U1] page size and rotation 
        int rotation = pageSize.getRotation();
        // [C10] 
        if (writer.isPdfX()) {
            if (thisBoxSize.containsKey("art") && thisBoxSize.containsKey("trim"))
                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("only.one.of.artbox.or.trimbox.can.exist.in.the.page"));
            if (!thisBoxSize.containsKey("art") && !thisBoxSize.containsKey("trim")) {
                if (thisBoxSize.containsKey("crop"))
                    thisBoxSize.put("trim", thisBoxSize.get("crop"));
                else
                    thisBoxSize.put("trim", new PdfRectangle(pageSize, pageSize.getRotation()));
            }
        }
        // [M1] 
        pageResources.addDefaultColorDiff(writer.getDefaultColorspace());
        if (writer.isRgbTransparencyBlending()) {
            PdfDictionary dcs = new PdfDictionary();
            dcs.put(PdfName.CS, PdfName.DEVICERGB);
            pageResources.addDefaultColorDiff(dcs);
        }
        PdfDictionary resources = pageResources.getResources();
        // we create the page dictionary 
        PdfPage page = new PdfPage(new PdfRectangle(pageSize, rotation), thisBoxSize, resources, rotation);
        page.put(PdfName.TABS, writer.getTabs());
        // we complete the page dictionary 
        // [C9] if there is XMP data to add: add it 
        if (xmpMetadata != null) {
            PdfStream xmp = new PdfStream(xmpMetadata);
            xmp.put(PdfName.TYPE, PdfName.METADATA);
            xmp.put(PdfName.SUBTYPE, PdfName.XML);
            PdfEncryption crypto = writer.getEncryption();
            if (crypto != null && !crypto.isMetadataEncrypted()) {
                PdfArray ar = new PdfArray();
                ar.add(PdfName.CRYPT);
                xmp.put(PdfName.FILTER, ar);
            }
            page.put(PdfName.METADATA, writer.addToBody(xmp).getIndirectReference());
        }
        // [U3] page actions: transition, duration, additional actions 
        if (this.transition != null) {
            page.put(PdfName.TRANS, this.transition.getTransitionDictionary());
            transition = null;
        }
        if (this.duration > 0) {
            page.put(PdfName.DUR, new PdfNumber(this.duration));
            duration = 0;
        }
        if (pageAA != null) {
            page.put(PdfName.AA, writer.addToBody(pageAA).getIndirectReference());
            pageAA = null;
        }
        // [U4] we add the thumbs 
        if (thumb != null) {
            page.put(PdfName.THUMB, thumb);
            thumb = null;
        }
        // [U8] we check if the userunit is defined 
        if (writer.getUserunit() > 0f) {
            page.put(PdfName.USERUNIT, new PdfNumber(writer.getUserunit()));
        }
        // [C5] and [C8] we add the annotations 
        if (annotationsImp.hasUnusedAnnotations()) {
            PdfArray array = annotationsImp.rotateAnnotations(writer, pageSize);
            if (array.size() != 0)
                page.put(PdfName.ANNOTS, array);
        }
        // [F12] we add tag info 
        if (writer.isTagged())
            page.put(PdfName.STRUCTPARENTS, new PdfNumber(writer.getCurrentPageNumber() - 1));
        if (text.size() > textEmptySize)
            text.endText();
        else
            text = null;
        writer.add(page, new PdfContents(writer.getDirectContentUnder(), graphics, text, writer.getDirectContent(), pageSize));
        // we initialize the new page 
        initPage();
    } catch (DocumentException de) {
        // maybe this never happens, but it's better to check. 
        throw new ExceptionConverter(de);
    } catch (IOException ioe) {
        throw new ExceptionConverter(ioe);
    }
    return true;
}
