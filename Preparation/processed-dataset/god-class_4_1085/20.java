/** Gets the width from the font according to the unicode char <CODE>c</CODE>.
     * If the <CODE>name</CODE> is null it's a symbolic font.
     * @param c the unicode char
     * @param name the glyph name
     * @return the width of the char
     */
@Override
int getRawWidth(int c, String name) {
    int[] metric = getMetricsTT(c);
    if (metric == null)
        return 0;
    return metric[1];
}
