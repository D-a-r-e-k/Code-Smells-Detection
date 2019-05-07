static int OnlyOneBitSet(long l) {
    int oneSeen = -1;
    for (int i = 0; i < 64; i++) if (((l >> i) & 1L) != 0L) {
        if (oneSeen >= 0)
            return -1;
        oneSeen = i;
    }
    return oneSeen;
}
