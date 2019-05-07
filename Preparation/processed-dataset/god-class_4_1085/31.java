/** Gets the glyph index and metrics for a character.
     * @param c the character
     * @return an <CODE>int</CODE> array with {glyph index, width}
     */
public int[] getMetricsTT(int c) {
    if (cmapExt != null)
        return cmapExt.get(new Integer(c));
    if (!fontSpecific && cmap31 != null)
        return cmap31.get(new Integer(c));
    if (fontSpecific && cmap10 != null)
        return cmap10.get(new Integer(c));
    if (cmap31 != null)
        return cmap31.get(new Integer(c));
    if (cmap10 != null)
        return cmap10.get(new Integer(c));
    return null;
}
