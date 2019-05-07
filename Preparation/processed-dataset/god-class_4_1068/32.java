private final int signum(int value) {
    if (value == 0) {
        return 0;
    }
    return value < 0 ? -1 : 1;
}
