/**
     * The pattern to use to generate the subtotal labels.  The grouping value of the cell will be the first arg.
     * The default value is "{0} Total".
     * @param pattern
     * @param locale
     */
public void setSubtotalLabel(String pattern, Locale locale) {
    this.subtotalDesc = new MessageFormat(pattern, locale);
}
