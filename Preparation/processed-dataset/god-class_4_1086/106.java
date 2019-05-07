/** Removes all the unreachable objects.
     * @return the number of indirect objects removed
     */
public int removeUnusedObjects() {
    boolean hits[] = new boolean[xrefObj.size()];
    removeUnusedNode(trailer, hits);
    int total = 0;
    if (partial) {
        for (int k = 1; k < hits.length; ++k) {
            if (!hits[k]) {
                xref[k * 2] = -1;
                xref[k * 2 + 1] = 0;
                xrefObj.set(k, null);
                ++total;
            }
        }
    } else {
        for (int k = 1; k < hits.length; ++k) {
            if (!hits[k]) {
                xrefObj.set(k, null);
                ++total;
            }
        }
    }
    return total;
}
