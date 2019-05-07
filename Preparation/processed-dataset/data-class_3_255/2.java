/**
     * Makes this instance an independent copy of <CODE>org</CODE>.
     *
     * @param org the original <CODE>ColumnText</CODE>
     * @return itself
     */
public ColumnText setACopy(ColumnText org) {
    setSimpleVars(org);
    if (org.bidiLine != null)
        bidiLine = new BidiLine(org.bidiLine);
    return this;
}
