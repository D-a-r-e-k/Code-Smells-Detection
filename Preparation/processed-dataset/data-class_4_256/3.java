void alterContents() throws IOException {
    for (Object element : pagesToContent.values()) {
        PageStamp ps = (PageStamp) element;
        PdfDictionary pageN = ps.pageN;
        markUsed(pageN);
        PdfArray ar = null;
        PdfObject content = PdfReader.getPdfObject(pageN.get(PdfName.CONTENTS), pageN);
        if (content == null) {
            ar = new PdfArray();
            pageN.put(PdfName.CONTENTS, ar);
        } else if (content.isArray()) {
            ar = (PdfArray) content;
            markUsed(ar);
        } else if (content.isStream()) {
            ar = new PdfArray();
            ar.add(pageN.get(PdfName.CONTENTS));
            pageN.put(PdfName.CONTENTS, ar);
        } else {
            ar = new PdfArray();
            pageN.put(PdfName.CONTENTS, ar);
        }
        ByteBuffer out = new ByteBuffer();
        if (ps.under != null) {
            out.append(PdfContents.SAVESTATE);
            applyRotation(pageN, out);
            out.append(ps.under.getInternalBuffer());
            out.append(PdfContents.RESTORESTATE);
        }
        if (ps.over != null)
            out.append(PdfContents.SAVESTATE);
        PdfStream stream = new PdfStream(out.toByteArray());
        stream.flateCompress(compressionLevel);
        ar.addFirst(addToBody(stream).getIndirectReference());
        out.reset();
        if (ps.over != null) {
            out.append(' ');
            out.append(PdfContents.RESTORESTATE);
            ByteBuffer buf = ps.over.getInternalBuffer();
            out.append(buf.getBuffer(), 0, ps.replacePoint);
            out.append(PdfContents.SAVESTATE);
            applyRotation(pageN, out);
            out.append(buf.getBuffer(), ps.replacePoint, buf.size() - ps.replacePoint);
            out.append(PdfContents.RESTORESTATE);
            stream = new PdfStream(out.toByteArray());
            stream.flateCompress(compressionLevel);
            ar.add(addToBody(stream).getIndirectReference());
        }
        alterResources(ps);
    }
}
