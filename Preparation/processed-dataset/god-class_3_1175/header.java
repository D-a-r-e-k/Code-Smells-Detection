void method0() { 
/**
     * A reference to the translet object for the transformation.
     */
private AbstractTranslet _translet;
/**
     * The sort order (ascending or descending) for each level of
     * <code>xsl:sort</code>
     */
private int[] _sortOrders;
/**
     * The type of comparison (text or number) for each level of
     * <code>xsl:sort</code>
     */
private int[] _types;
/**
     * The Locale for each level of <code>xsl:sort</code>, based on any lang
     * attribute or the default Locale.
     */
private Locale[] _locales;
/**
     * The Collator object in effect for each level of <code>xsl:sort</code>
     */
private Collator[] _collators;
/**
     * Case ordering for each level of <code>xsl:sort</code>.
     */
private String[] _caseOrders;
}
