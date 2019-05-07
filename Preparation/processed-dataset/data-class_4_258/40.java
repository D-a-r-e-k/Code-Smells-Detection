@Override
protected int[] getRawCharBBox(int c, String name) {
    HashMap<Integer, int[]> map = null;
    if (name == null || cmap31 == null)
        map = cmap10;
    else
        map = cmap31;
    if (map == null)
        return null;
    int metric[] = map.get(new Integer(c));
    if (metric == null || bboxes == null)
        return null;
    return bboxes[metric[0]];
}
