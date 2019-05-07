protected void addRangeUni(HashSet<Integer> longTag, boolean subsetp) {
    if (!subsetp && (subsetRanges != null || directoryOffset > 0)) {
        int[] rg = subsetRanges == null && directoryOffset > 0 ? new int[] { 0, 0xffff } : compactRanges(subsetRanges);
        HashMap<Integer, int[]> usemap;
        if (!fontSpecific && cmap31 != null)
            usemap = cmap31;
        else if (fontSpecific && cmap10 != null)
            usemap = cmap10;
        else if (cmap31 != null)
            usemap = cmap31;
        else
            usemap = cmap10;
        for (Map.Entry<Integer, int[]> e : usemap.entrySet()) {
            int[] v = e.getValue();
            Integer gi = new Integer(v[0]);
            if (longTag.contains(gi))
                continue;
            int c = e.getKey().intValue();
            boolean skip = true;
            for (int k = 0; k < rg.length; k += 2) {
                if (c >= rg[k] && c <= rg[k + 1]) {
                    skip = false;
                    break;
                }
            }
            if (!skip)
                longTag.add(gi);
        }
    }
}
