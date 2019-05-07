void replacePage(PdfReader r, int pageImported, int pageReplaced) {
    PdfDictionary pageN = reader.getPageN(pageReplaced);
    if (pagesToContent.containsKey(pageN))
        throw new IllegalStateException(MessageLocalization.getComposedMessage("this.page.cannot.be.replaced.new.content.was.already.added"));
    PdfImportedPage p = getImportedPage(r, pageImported);
    PdfDictionary dic2 = reader.getPageNRelease(pageReplaced);
    dic2.remove(PdfName.RESOURCES);
    dic2.remove(PdfName.CONTENTS);
    moveRectangle(dic2, r, pageImported, PdfName.MEDIABOX, "media");
    moveRectangle(dic2, r, pageImported, PdfName.CROPBOX, "crop");
    moveRectangle(dic2, r, pageImported, PdfName.TRIMBOX, "trim");
    moveRectangle(dic2, r, pageImported, PdfName.ARTBOX, "art");
    moveRectangle(dic2, r, pageImported, PdfName.BLEEDBOX, "bleed");
    dic2.put(PdfName.ROTATE, new PdfNumber(r.getPageRotation(pageImported)));
    PdfContentByte cb = getOverContent(pageReplaced);
    cb.addTemplate(p, 0, 0);
    PageStamp ps = pagesToContent.get(pageN);
    ps.replacePoint = ps.over.getInternalBuffer().size();
}
