static boolean equalsn(byte a1[], byte a2[]) {
    int length = a2.length;
    for (int k = 0; k < length; ++k) {
        if (a1[k] != a2[k])
            return false;
    }
    return true;
}
