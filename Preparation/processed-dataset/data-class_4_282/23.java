/**
   * Record a row of Fragment objects into the buffer.
   *
   * @param row      the index of the row
   * @param xstart   the starting position along the row
   * @param xend     the ending position along the row
   * @param context  the RasterContext from which to copy the Fragments
   */
private void recordRow(int row, int xstart, int xend, RasterContext context) {
    Fragment source[] = context.fragment;
    int indexBase = row * width;
    synchronized (lock[row]) {
        for (int x = xstart; x < xend; x++) {
            Fragment f = source[x];
            if (f == null)
                continue;
            int index = indexBase + x;
            Fragment current = fragment[index];
            if (f.getDepth() < current.getDepth())
                fragment[index] = f.insertNextFragment(current);
            else
                fragment[index] = current.insertNextFragment(f);
        }
    }
}
