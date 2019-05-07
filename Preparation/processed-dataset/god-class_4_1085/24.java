protected static int[] compactRanges(ArrayList<int[]> ranges) {
    ArrayList<int[]> simp = new ArrayList<int[]>();
    for (int k = 0; k < ranges.size(); ++k) {
        int[] r = ranges.get(k);
        for (int j = 0; j < r.length; j += 2) {
            simp.add(new int[] { Math.max(0, Math.min(r[j], r[j + 1])), Math.min(0xffff, Math.max(r[j], r[j + 1])) });
        }
    }
    for (int k1 = 0; k1 < simp.size() - 1; ++k1) {
        for (int k2 = k1 + 1; k2 < simp.size(); ++k2) {
            int[] r1 = simp.get(k1);
            int[] r2 = simp.get(k2);
            if (r1[0] >= r2[0] && r1[0] <= r2[1] || r1[1] >= r2[0] && r1[0] <= r2[1]) {
                r1[0] = Math.min(r1[0], r2[0]);
                r1[1] = Math.max(r1[1], r2[1]);
                simp.remove(k2);
                --k2;
            }
        }
    }
    int[] s = new int[simp.size() * 2];
    for (int k = 0; k < simp.size(); ++k) {
        int[] r = simp.get(k);
        s[k * 2] = r[0];
        s[k * 2 + 1] = r[1];
    }
    return s;
}
