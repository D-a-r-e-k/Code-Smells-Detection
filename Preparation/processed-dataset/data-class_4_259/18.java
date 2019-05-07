private boolean equalsArray(byte ar1[], byte ar2[], int size) {
    for (int k = 0; k < size; ++k) {
        if (ar1[k] != ar2[k])
            return false;
    }
    return true;
}
