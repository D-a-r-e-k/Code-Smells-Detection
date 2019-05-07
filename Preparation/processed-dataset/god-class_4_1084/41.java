/** Implements an action in an area.
     * @param action the <CODE>PdfAction</CODE>
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
    addAnnotation(new PdfAnnotation(writer, llx, lly, urx, ury, action));
}
