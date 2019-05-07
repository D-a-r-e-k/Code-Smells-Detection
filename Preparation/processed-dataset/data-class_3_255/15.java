/**
     * Simplified method for rectangular columns.
     *
     * @param llx the lower left x corner
     * @param lly the lower left y corner
     * @param urx the upper right x corner
     * @param ury the upper right y corner
     * @param leading the leading
     * @param alignment the column alignment
     */
public void setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment) {
    setLeading(leading);
    this.alignment = alignment;
    setSimpleColumn(llx, lly, urx, ury);
}
