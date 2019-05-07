/** Eliminates shared streams if they exist. */
public void eliminateSharedStreams() {
    if (!sharedStreams)
        return;
    sharedStreams = false;
    if (pageRefs.size() == 1)
        return;
    ArrayList<PRIndirectReference> newRefs = new ArrayList<PRIndirectReference>();
    ArrayList<PRStream> newStreams = new ArrayList<PRStream>();
    IntHashtable visited = new IntHashtable();
    for (int k = 1; k <= pageRefs.size(); ++k) {
        PdfDictionary page = pageRefs.getPageN(k);
        if (page == null)
            continue;
        PdfObject contents = getPdfObject(page.get(PdfName.CONTENTS));
        if (contents == null)
            continue;
        if (contents.isStream()) {
            PRIndirectReference ref = (PRIndirectReference) page.get(PdfName.CONTENTS);
            if (visited.containsKey(ref.getNumber())) {
                // need to duplicate 
                newRefs.add(ref);
                newStreams.add(new PRStream((PRStream) contents, null));
            } else
                visited.put(ref.getNumber(), 1);
        } else if (contents.isArray()) {
            PdfArray array = (PdfArray) contents;
            for (int j = 0; j < array.size(); ++j) {
                PRIndirectReference ref = (PRIndirectReference) array.getPdfObject(j);
                if (visited.containsKey(ref.getNumber())) {
                    // need to duplicate 
                    newRefs.add(ref);
                    newStreams.add(new PRStream((PRStream) getPdfObject(ref), null));
                } else
                    visited.put(ref.getNumber(), 1);
            }
        }
    }
    if (newStreams.isEmpty())
        return;
    for (int k = 0; k < newStreams.size(); ++k) {
        xrefObj.add(newStreams.get(k));
        PRIndirectReference ref = newRefs.get(k);
        ref.setNumber(xrefObj.size() - 1, 0);
    }
}
