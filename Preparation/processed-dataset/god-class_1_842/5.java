public void check(Parse cell, long value) {
    if (parseLong(cell) == value) {
        right(cell);
    } else {
        wrong(cell, Long.toString(value));
    }
}
