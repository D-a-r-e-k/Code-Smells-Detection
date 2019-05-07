/**
     * Sets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @param kern the kerning to apply in normalized 1000 units
     * @return <code>true</code> if the kerning was applied, <code>false</code> otherwise
     */
@Override
public boolean setKerning(int char1, int char2, int kern) {
    int metrics[] = getMetricsTT(char1);
    if (metrics == null)
        return false;
    int c1 = metrics[0];
    metrics = getMetricsTT(char2);
    if (metrics == null)
        return false;
    int c2 = metrics[0];
    kerning.put((c1 << 16) + c2, kern);
    return true;
}
