/**
     * Creates an independent duplicated of the instance <CODE>org</CODE>.
     *
     * @param org the original <CODE>ColumnText</CODE>
     * @return the duplicated
     */
public static ColumnText duplicate(ColumnText org) {
    ColumnText ct = new ColumnText(null);
    ct.setACopy(org);
    return ct;
}
