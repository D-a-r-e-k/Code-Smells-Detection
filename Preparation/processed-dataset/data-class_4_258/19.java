/** Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied
     */
@Override
public int getKerning(int char1, int char2) {
    int metrics[] = getMetricsTT(char1);
    if (metrics == null)
        return 0;
    int c1 = metrics[0];
    metrics = getMetricsTT(char2);
    if (metrics == null)
        return 0;
    int c2 = metrics[0];
    return kerning.get((c1 << 16) + c2);
}
