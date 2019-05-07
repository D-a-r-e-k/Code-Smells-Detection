private void flatFreeTextFields() {
    if (append)
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("freetext.flattening.is.not.supported.in.append.mode"));
    for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
        PdfDictionary pageDic = reader.getPageN(page);
        PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
        if (annots == null)
            continue;
        for (int idx = 0; idx < annots.size(); ++idx) {
            PdfObject annoto = annots.getDirectObject(idx);
            if (annoto instanceof PdfIndirectReference && !annoto.isIndirect())
                continue;
            PdfDictionary annDic = (PdfDictionary) annoto;
            if (!((PdfName) annDic.get(PdfName.SUBTYPE)).equals(PdfName.FREETEXT))
                continue;
            PdfNumber ff = annDic.getAsNumber(PdfName.F);
            int flags = ff != null ? ff.intValue() : 0;
            if ((flags & PdfFormField.FLAGS_PRINT) != 0 && (flags & PdfFormField.FLAGS_HIDDEN) == 0) {
                PdfObject obj1 = annDic.get(PdfName.AP);
                if (obj1 == null)
                    continue;
                PdfDictionary appDic = obj1 instanceof PdfIndirectReference ? (PdfDictionary) PdfReader.getPdfObject(obj1) : (PdfDictionary) obj1;
                PdfObject obj = appDic.get(PdfName.N);
                PdfAppearance app = null;
                PdfObject objReal = PdfReader.getPdfObject(obj);
                if (obj instanceof PdfIndirectReference && !obj.isIndirect())
                    app = new PdfAppearance((PdfIndirectReference) obj);
                else if (objReal instanceof PdfStream) {
                    ((PdfDictionary) objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                    app = new PdfAppearance((PdfIndirectReference) obj);
                } else {
                    if (objReal.isDictionary()) {
                        PdfName as_p = appDic.getAsName(PdfName.AS);
                        if (as_p != null) {
                            PdfIndirectReference iref = (PdfIndirectReference) ((PdfDictionary) objReal).get(as_p);
                            if (iref != null) {
                                app = new PdfAppearance(iref);
                                if (iref.isIndirect()) {
                                    objReal = PdfReader.getPdfObject(iref);
                                    ((PdfDictionary) objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                                }
                            }
                        }
                    }
                }
                if (app != null) {
                    Rectangle box = PdfReader.getNormalizedRectangle(annDic.getAsArray(PdfName.RECT));
                    PdfContentByte cb = getOverContent(page);
                    cb.setLiteral("Q ");
                    cb.addTemplate(app, box.getLeft(), box.getBottom());
                    cb.setLiteral("q ");
                }
            }
        }
        for (int idx = 0; idx < annots.size(); ++idx) {
            PdfDictionary annot = annots.getAsDict(idx);
            if (annot != null) {
                if (PdfName.FREETEXT.equals(annot.get(PdfName.SUBTYPE))) {
                    annots.remove(idx);
                    --idx;
                }
            }
        }
        if (annots.isEmpty()) {
            PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
            pageDic.remove(PdfName.ANNOTS);
        }
    }
}
