//	[C5] named objects: local destinations, javascript, embedded files 
/**
     * Implements a link to other part of the document. The jump will
     * be made to a local destination with the same name, that must exist.
     * @param name the name for this link
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
void localGoto(String name, float llx, float lly, float urx, float ury) {
    PdfAction action = getLocalGotoAction(name);
    annotationsImp.addPlainAnnotation(new PdfAnnotation(writer, llx, lly, urx, ury, action));
}
