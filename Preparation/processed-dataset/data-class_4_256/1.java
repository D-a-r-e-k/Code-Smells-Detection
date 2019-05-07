void close(HashMap<String, String> moreInfo) throws IOException {
    if (closed)
        return;
    if (useVp) {
        reader.setViewerPreferences(viewerPreferences);
        markUsed(reader.getTrailer().get(PdfName.ROOT));
    }
    if (flat)
        flatFields();
    if (flatFreeText)
        flatFreeTextFields();
    addFieldResources();
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary pages = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.PAGES));
    pages.put(PdfName.ITXT, new PdfString(Document.getRelease()));
    markUsed(pages);
    PdfDictionary acroForm = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), reader.getCatalog());
    if (acroFields != null && acroFields.getXfa().isChanged()) {
        markUsed(acroForm);
        if (!flat)
            acroFields.getXfa().setXfa(this);
    }
    if (sigFlags != 0) {
        if (acroForm != null) {
            acroForm.put(PdfName.SIGFLAGS, new PdfNumber(sigFlags));
            markUsed(acroForm);
            markUsed(catalog);
        }
    }
    closed = true;
    addSharedObjectsToBody();
    setOutlines();
    setJavaScript();
    addFileAttachments();
    if (openAction != null) {
        catalog.put(PdfName.OPENACTION, openAction);
    }
    if (pdf.pageLabels != null)
        catalog.put(PdfName.PAGELABELS, pdf.pageLabels.getDictionary(this));
    // OCG 
    if (!documentOCG.isEmpty()) {
        fillOCProperties(false);
        PdfDictionary ocdict = catalog.getAsDict(PdfName.OCPROPERTIES);
        if (ocdict == null) {
            reader.getCatalog().put(PdfName.OCPROPERTIES, OCProperties);
        } else {
            ocdict.put(PdfName.OCGS, OCProperties.get(PdfName.OCGS));
            PdfDictionary ddict = ocdict.getAsDict(PdfName.D);
            if (ddict == null) {
                ddict = new PdfDictionary();
                ocdict.put(PdfName.D, ddict);
            }
            ddict.put(PdfName.ORDER, OCProperties.getAsDict(PdfName.D).get(PdfName.ORDER));
            ddict.put(PdfName.RBGROUPS, OCProperties.getAsDict(PdfName.D).get(PdfName.RBGROUPS));
            ddict.put(PdfName.OFF, OCProperties.getAsDict(PdfName.D).get(PdfName.OFF));
            ddict.put(PdfName.AS, OCProperties.getAsDict(PdfName.D).get(PdfName.AS));
        }
    }
    // metadata 
    int skipInfo = -1;
    PRIndirectReference iInfo = (PRIndirectReference) reader.getTrailer().get(PdfName.INFO);
    PdfDictionary oldInfo = (PdfDictionary) PdfReader.getPdfObject(iInfo);
    String producer = null;
    if (iInfo != null)
        skipInfo = iInfo.getNumber();
    if (oldInfo != null && oldInfo.get(PdfName.PRODUCER) != null)
        producer = oldInfo.getAsString(PdfName.PRODUCER).toUnicodeString();
    if (producer == null) {
        producer = Document.getVersion();
    } else if (producer.indexOf(Document.getProduct()) == -1) {
        StringBuffer buf = new StringBuffer(producer);
        buf.append("; modified using ");
        buf.append(Document.getVersion());
        producer = buf.toString();
    }
    // XMP 
    byte[] altMetadata = null;
    PdfObject xmpo = PdfReader.getPdfObject(catalog.get(PdfName.METADATA));
    if (xmpo != null && xmpo.isStream()) {
        altMetadata = PdfReader.getStreamBytesRaw((PRStream) xmpo);
        PdfReader.killIndirect(catalog.get(PdfName.METADATA));
    }
    if (xmpMetadata != null) {
        altMetadata = xmpMetadata;
    }
    // if there is XMP data to add: add it 
    PdfDate date = new PdfDate();
    if (altMetadata != null) {
        PdfStream xmp;
        try {
            XmpReader xmpr = new XmpReader(altMetadata);
            if (!(xmpr.replaceNode("http://ns.adobe.com/pdf/1.3/", "Producer", producer) || xmpr.replaceDescriptionAttribute("http://ns.adobe.com/pdf/1.3/", "Producer", producer)))
                xmpr.add("rdf:Description", "http://ns.adobe.com/pdf/1.3/", "pdf:Producer", producer);
            if (!(xmpr.replaceNode("http://ns.adobe.com/xap/1.0/", "ModifyDate", date.getW3CDate()) || xmpr.replaceDescriptionAttribute("http://ns.adobe.com/xap/1.0/", "ModifyDate", date.getW3CDate())))
                xmpr.add("rdf:Description", "http://ns.adobe.com/xap/1.0/", "xmp:ModifyDate", date.getW3CDate());
            if (!(xmpr.replaceNode("http://ns.adobe.com/xap/1.0/", "MetadataDate", date.getW3CDate()) || xmpr.replaceDescriptionAttribute("http://ns.adobe.com/xap/1.0/", "MetadataDate", date.getW3CDate()))) {
            }
            xmp = new PdfStream(xmpr.serializeDoc());
        } catch (SAXException e) {
            xmp = new PdfStream(altMetadata);
        } catch (IOException e) {
            xmp = new PdfStream(altMetadata);
        }
        xmp.put(PdfName.TYPE, PdfName.METADATA);
        xmp.put(PdfName.SUBTYPE, PdfName.XML);
        if (crypto != null && !crypto.isMetadataEncrypted()) {
            PdfArray ar = new PdfArray();
            ar.add(PdfName.CRYPT);
            xmp.put(PdfName.FILTER, ar);
        }
        if (append && xmpo != null) {
            body.add(xmp, xmpo.getIndRef());
        } else {
            catalog.put(PdfName.METADATA, body.add(xmp).getIndirectReference());
            markUsed(catalog);
        }
    }
    try {
        file.reOpen();
        alterContents();
        int rootN = ((PRIndirectReference) reader.trailer.get(PdfName.ROOT)).getNumber();
        if (append) {
            int keys[] = marked.getKeys();
            for (int k = 0; k < keys.length; ++k) {
                int j = keys[k];
                PdfObject obj = reader.getPdfObjectRelease(j);
                if (obj != null && skipInfo != j && j < initialXrefSize) {
                    addToBody(obj, j, j != rootN);
                }
            }
            for (int k = initialXrefSize; k < reader.getXrefSize(); ++k) {
                PdfObject obj = reader.getPdfObject(k);
                if (obj != null) {
                    addToBody(obj, getNewObjectNumber(reader, k, 0));
                }
            }
        } else {
            for (int k = 1; k < reader.getXrefSize(); ++k) {
                PdfObject obj = reader.getPdfObjectRelease(k);
                if (obj != null && skipInfo != k) {
                    addToBody(obj, getNewObjectNumber(reader, k, 0), k != rootN);
                }
            }
        }
    } finally {
        try {
            file.close();
        } catch (Exception e) {
        }
    }
    PdfIndirectReference encryption = null;
    PdfObject fileID = null;
    if (crypto != null) {
        if (append) {
            encryption = reader.getCryptoRef();
        } else {
            PdfIndirectObject encryptionObject = addToBody(crypto.getEncryptionDictionary(), false);
            encryption = encryptionObject.getIndirectReference();
        }
        fileID = crypto.getFileID();
    } else
        fileID = PdfEncryption.createInfoId(PdfEncryption.createDocumentId());
    PRIndirectReference iRoot = (PRIndirectReference) reader.trailer.get(PdfName.ROOT);
    PdfIndirectReference root = new PdfIndirectReference(0, getNewObjectNumber(reader, iRoot.getNumber(), 0));
    PdfIndirectReference info = null;
    PdfDictionary newInfo = new PdfDictionary();
    if (oldInfo != null) {
        for (Object element : oldInfo.getKeys()) {
            PdfName key = (PdfName) element;
            PdfObject value = PdfReader.getPdfObject(oldInfo.get(key));
            newInfo.put(key, value);
        }
    }
    if (moreInfo != null) {
        for (Map.Entry<String, String> entry : moreInfo.entrySet()) {
            String key = entry.getKey();
            PdfName keyName = new PdfName(key);
            String value = entry.getValue();
            if (value == null)
                newInfo.remove(keyName);
            else
                newInfo.put(keyName, new PdfString(value, PdfObject.TEXT_UNICODE));
        }
    }
    newInfo.put(PdfName.MODDATE, date);
    newInfo.put(PdfName.PRODUCER, new PdfString(producer));
    if (append) {
        if (iInfo == null)
            info = addToBody(newInfo, false).getIndirectReference();
        else
            info = addToBody(newInfo, iInfo.getNumber(), false).getIndirectReference();
    } else {
        info = addToBody(newInfo, false).getIndirectReference();
    }
    // write the cross-reference table of the body 
    body.writeCrossReferenceTable(os, root, info, encryption, fileID, prevxref);
    if (fullCompression) {
        os.write(getISOBytes("startxref\n"));
        os.write(getISOBytes(String.valueOf(body.offset())));
        os.write(getISOBytes("\n%%EOF\n"));
    } else {
        PdfTrailer trailer = new PdfTrailer(body.size(), body.offset(), root, info, encryption, fileID, prevxref);
        trailer.toPdf(this, os);
    }
    os.flush();
    if (isCloseStream())
        os.close();
    reader.close();
}
