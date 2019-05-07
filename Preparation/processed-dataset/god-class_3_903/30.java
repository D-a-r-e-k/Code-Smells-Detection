private static boolean EqualNonAsciiMoveIndices(int[] moves1, int[] moves2) {
    if (moves1 == moves2)
        return true;
    if (moves1 == null || moves2 == null)
        return false;
    if (moves1.length != moves2.length)
        return false;
    for (int i = 0; i < moves1.length; i++) {
        if (moves1[i] != moves2[i])
            return false;
    }
    return true;
}
