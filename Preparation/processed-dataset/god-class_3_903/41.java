static int NumberOfBitsSet(long l) {
    int ret = 0;
    for (int i = 0; i < 63; i++) if (((l >> i) & 1L) != 0L)
        ret++;
    return ret;
}
