void addAnnotation(PdfAnnotation annot, PdfDictionary pageN) {
    try {
        ArrayList<PdfAnnotation> allAnnots = new ArrayList<PdfAnnotation>();
        if (annot.isForm()) {
            fieldsAdded = true;
            getAcroFields();
            PdfFormField field = (PdfFormField) annot;
            if (field.getParent() != null)
                return;
            expandFields(field, allAnnots);
        } else
            allAnnots.add(annot);
        for (int k = 0; k < allAnnots.size(); ++k) {
            annot = allAnnots.get(k);
            if (annot.getPlaceInPage() > 0)
                pageN = reader.getPageN(annot.getPlaceInPage());
            if (annot.isForm()) {
                if (!annot.isUsed()) {
                    HashSet<PdfTemplate> templates = annot.getTemplates();
                    if (templates != null)
                        fieldTemplates.addAll(templates);
                }
                PdfFormField field = (PdfFormField) annot;
                if (field.getParent() == null)
                    addDocumentField(field.getIndirectReference());
            }
            if (annot.isAnnotation()) {
                PdfObject pdfobj = PdfReader.getPdfObject(pageN.get(PdfName.ANNOTS), pageN);
                PdfArray annots = null;
                if (pdfobj == null || !pdfobj.isArray()) {
                    annots = new PdfArray();
                    pageN.put(PdfName.ANNOTS, annots);
                    markUsed(pageN);
                } else
                    annots = (PdfArray) pdfobj;
                annots.add(annot.getIndirectReference());
                markUsed(annots);
                if (!annot.isUsed()) {
                    PdfRectangle rect = (PdfRectangle) annot.get(PdfName.RECT);
                    if (rect != null && (rect.left() != 0 || rect.right() != 0 || rect.top() != 0 || rect.bottom() != 0)) {
                        int rotation = reader.getPageRotation(pageN);
                        Rectangle pageSize = reader.getPageSizeWithRotation(pageN);
                        switch(rotation) {
                            case 90:
                                annot.put(PdfName.RECT, new PdfRectangle(pageSize.getTop() - rect.top(), rect.right(), pageSize.getTop() - rect.bottom(), rect.left()));
                                break;
                            case 180:
                                annot.put(PdfName.RECT, new PdfRectangle(pageSize.getRight() - rect.left(), pageSize.getTop() - rect.bottom(), pageSize.getRight() - rect.right(), pageSize.getTop() - rect.top()));
                                break;
                            case 270:
                                annot.put(PdfName.RECT, new PdfRectangle(rect.bottom(), pageSize.getRight() - rect.left(), rect.top(), pageSize.getRight() - rect.right()));
                                break;
                        }
                    }
                }
            }
            if (!annot.isUsed()) {
                annot.setUsed();
                addToBody(annot, annot.getIndirectReference());
            }
        }
    } catch (IOException e) {
        throw new ExceptionConverter(e);
    }
}
