/**
     * Gets the <CODE>PdfCatalog</CODE>-object.
     *
     * @param pages an indirect reference to this document pages
     * @return <CODE>PdfCatalog</CODE>
     */
PdfCatalog getCatalog(PdfIndirectReference pages) {
    PdfCatalog catalog = new PdfCatalog(pages, writer);
    // [C1] outlines 
    if (rootOutline.getKids().size() > 0) {
        catalog.put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
        catalog.put(PdfName.OUTLINES, rootOutline.indirectReference());
    }
    // [C2] version 
    writer.getPdfVersion().addToCatalog(catalog);
    // [C3] preferences 
    viewerPreferences.addToCatalog(catalog);
    // [C4] pagelabels 
    if (pageLabels != null) {
        catalog.put(PdfName.PAGELABELS, pageLabels.getDictionary(writer));
    }
    // [C5] named objects 
    catalog.addNames(localDestinations, getDocumentLevelJS(), documentFileAttachment, writer);
    // [C6] actions 
    if (openActionName != null) {
        PdfAction action = getLocalGotoAction(openActionName);
        catalog.setOpenAction(action);
    } else if (openActionAction != null)
        catalog.setOpenAction(openActionAction);
    if (additionalActions != null) {
        catalog.setAdditionalActions(additionalActions);
    }
    // [C7] portable collections 
    if (collection != null) {
        catalog.put(PdfName.COLLECTION, collection);
    }
    // [C8] AcroForm 
    if (annotationsImp.hasValidAcroForm()) {
        try {
            catalog.put(PdfName.ACROFORM, writer.addToBody(annotationsImp.getAcroForm()).getIndirectReference());
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
    return catalog;
}
