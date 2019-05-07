void flatFields() {
    if (append)
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.flattening.is.not.supported.in.append.mode"));
    getAcroFields();
    HashMap<String, Item> fields = acroFields.getFields();
    if (fieldsAdded && partialFlattening.isEmpty()) {
        for (String s : fields.keySet()) {
            partialFlattening.add(s);
        }
    }
    PdfDictionary acroForm = reader.getCatalog().getAsDict(PdfName.ACROFORM);
    PdfArray acroFds = null;
    if (acroForm != null) {
        acroFds = (PdfArray) PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
    }
    for (Map.Entry<String, Item> entry : fields.entrySet()) {
        String name = entry.getKey();
        if (!partialFlattening.isEmpty() && !partialFlattening.contains(name))
            continue;
        AcroFields.Item item = entry.getValue();
        for (int k = 0; k < item.size(); ++k) {
            PdfDictionary merged = item.getMerged(k);
            PdfNumber ff = merged.getAsNumber(PdfName.F);
            int flags = 0;
            if (ff != null)
                flags = ff.intValue();
            int page = item.getPage(k).intValue();
            PdfDictionary appDic = merged.getAsDict(PdfName.AP);
            if (appDic != null && (flags & PdfFormField.FLAGS_PRINT) != 0 && (flags & PdfFormField.FLAGS_HIDDEN) == 0) {
                PdfObject obj = appDic.get(PdfName.N);
                PdfAppearance app = null;
                if (obj != null) {
                    PdfObject objReal = PdfReader.getPdfObject(obj);
                    if (obj instanceof PdfIndirectReference && !obj.isIndirect())
                        app = new PdfAppearance((PdfIndirectReference) obj);
                    else if (objReal instanceof PdfStream) {
                        ((PdfDictionary) objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                        app = new PdfAppearance((PdfIndirectReference) obj);
                    } else {
                        if (objReal != null && objReal.isDictionary()) {
                            PdfName as = merged.getAsName(PdfName.AS);
                            if (as != null) {
                                PdfIndirectReference iref = (PdfIndirectReference) ((PdfDictionary) objReal).get(as);
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
                }
                if (app != null) {
                    Rectangle box = PdfReader.getNormalizedRectangle(merged.getAsArray(PdfName.RECT));
                    PdfContentByte cb = getOverContent(page);
                    cb.setLiteral("Q ");
                    cb.addTemplate(app, box.getLeft(), box.getBottom());
                    cb.setLiteral("q ");
                }
            }
            if (partialFlattening.isEmpty())
                continue;
            PdfDictionary pageDic = reader.getPageN(page);
            PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
            if (annots == null)
                continue;
            for (int idx = 0; idx < annots.size(); ++idx) {
                PdfObject ran = annots.getPdfObject(idx);
                if (!ran.isIndirect())
                    continue;
                PdfObject ran2 = item.getWidgetRef(k);
                if (!ran2.isIndirect())
                    continue;
                if (((PRIndirectReference) ran).getNumber() == ((PRIndirectReference) ran2).getNumber()) {
                    annots.remove(idx--);
                    PRIndirectReference wdref = (PRIndirectReference) ran2;
                    while (true) {
                        PdfDictionary wd = (PdfDictionary) PdfReader.getPdfObject(wdref);
                        PRIndirectReference parentRef = (PRIndirectReference) wd.get(PdfName.PARENT);
                        PdfReader.killIndirect(wdref);
                        if (parentRef == null) {
                            // reached AcroForm 
                            for (int fr = 0; fr < acroFds.size(); ++fr) {
                                PdfObject h = acroFds.getPdfObject(fr);
                                if (h.isIndirect() && ((PRIndirectReference) h).getNumber() == wdref.getNumber()) {
                                    acroFds.remove(fr);
                                    --fr;
                                }
                            }
                            break;
                        }
                        PdfDictionary parent = (PdfDictionary) PdfReader.getPdfObject(parentRef);
                        PdfArray kids = parent.getAsArray(PdfName.KIDS);
                        for (int fr = 0; fr < kids.size(); ++fr) {
                            PdfObject h = kids.getPdfObject(fr);
                            if (h.isIndirect() && ((PRIndirectReference) h).getNumber() == wdref.getNumber()) {
                                kids.remove(fr);
                                --fr;
                            }
                        }
                        if (!kids.isEmpty())
                            break;
                        wdref = parentRef;
                    }
                }
            }
            if (annots.isEmpty()) {
                PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
                pageDic.remove(PdfName.ANNOTS);
            }
        }
    }
    if (!fieldsAdded && partialFlattening.isEmpty()) {
        for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
            PdfDictionary pageDic = reader.getPageN(page);
            PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
            if (annots == null)
                continue;
            for (int idx = 0; idx < annots.size(); ++idx) {
                PdfObject annoto = annots.getDirectObject(idx);
                if (annoto instanceof PdfIndirectReference && !annoto.isIndirect())
                    continue;
                if (!annoto.isDictionary() || PdfName.WIDGET.equals(((PdfDictionary) annoto).get(PdfName.SUBTYPE))) {
                    annots.remove(idx);
                    --idx;
                }
            }
            if (annots.isEmpty()) {
                PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
                pageDic.remove(PdfName.ANNOTS);
            }
        }
        eliminateAcroformObjects();
    }
}
