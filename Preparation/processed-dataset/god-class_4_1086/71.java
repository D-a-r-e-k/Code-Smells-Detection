/**
     * Sets the tampered state. A tampered PdfReader cannot be reused in PdfStamper.
     * @param tampered the tampered state
     */
public void setTampered(boolean tampered) {
    this.tampered = tampered;
    pageRefs.keepPages();
}
