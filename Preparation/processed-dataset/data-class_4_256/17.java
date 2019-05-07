void insertPage(int pageNumber, Rectangle mediabox) {
    Rectangle media = new Rectangle(mediabox);
    int rotation = media.getRotation() % 360;
    PdfDictionary page = new PdfDictionary(PdfName.PAGE);
    PdfDictionary resources = new PdfDictionary();
    PdfArray procset = new PdfArray();
    procset.add(PdfName.PDF);
    procset.add(PdfName.TEXT);
    procset.add(PdfName.IMAGEB);
    procset.add(PdfName.IMAGEC);
    procset.add(PdfName.IMAGEI);
    resources.put(PdfName.PROCSET, procset);
    page.put(PdfName.RESOURCES, resources);
    page.put(PdfName.ROTATE, new PdfNumber(rotation));
    page.put(PdfName.MEDIABOX, new PdfRectangle(media, rotation));
    PRIndirectReference pref = reader.addPdfObject(page);
    PdfDictionary parent;
    PRIndirectReference parentRef;
    if (pageNumber > reader.getNumberOfPages()) {
        PdfDictionary lastPage = reader.getPageNRelease(reader.getNumberOfPages());
        parentRef = (PRIndirectReference) lastPage.get(PdfName.PARENT);
        parentRef = new PRIndirectReference(reader, parentRef.getNumber());
        parent = (PdfDictionary) PdfReader.getPdfObject(parentRef);
        PdfArray kids = (PdfArray) PdfReader.getPdfObject(parent.get(PdfName.KIDS), parent);
        kids.add(pref);
        markUsed(kids);
        reader.pageRefs.insertPage(pageNumber, pref);
    } else {
        if (pageNumber < 1)
            pageNumber = 1;
        PdfDictionary firstPage = reader.getPageN(pageNumber);
        PRIndirectReference firstPageRef = reader.getPageOrigRef(pageNumber);
        reader.releasePage(pageNumber);
        parentRef = (PRIndirectReference) firstPage.get(PdfName.PARENT);
        parentRef = new PRIndirectReference(reader, parentRef.getNumber());
        parent = (PdfDictionary) PdfReader.getPdfObject(parentRef);
        PdfArray kids = (PdfArray) PdfReader.getPdfObject(parent.get(PdfName.KIDS), parent);
        int len = kids.size();
        int num = firstPageRef.getNumber();
        for (int k = 0; k < len; ++k) {
            PRIndirectReference cur = (PRIndirectReference) kids.getPdfObject(k);
            if (num == cur.getNumber()) {
                kids.add(k, pref);
                break;
            }
        }
        if (len == kids.size())
            throw new RuntimeException(MessageLocalization.getComposedMessage("internal.inconsistence"));
        markUsed(kids);
        reader.pageRefs.insertPage(pageNumber, pref);
        correctAcroFieldPages(pageNumber);
    }
    page.put(PdfName.PARENT, parentRef);
    while (parent != null) {
        markUsed(parent);
        PdfNumber count = (PdfNumber) PdfReader.getPdfObjectRelease(parent.get(PdfName.COUNT));
        parent.put(PdfName.COUNT, new PdfNumber(count.intValue() + 1));
        parent = parent.getAsDict(PdfName.PARENT);
    }
}
