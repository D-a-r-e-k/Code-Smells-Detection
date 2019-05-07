private void validateTree(int pos) {
    int child1 = 2 * pos + 1, child2 = 2 * pos + 2;
    if (child1 < photon.length) {
        validateLowerBranch(child1, photon[pos].axis, median(pos, photon[pos].axis));
        validateTree(child1);
    }
    if (child2 < photon.length) {
        validateUpperBranch(child2, photon[pos].axis, median(pos, photon[pos].axis));
        validateTree(child2);
    }
}
