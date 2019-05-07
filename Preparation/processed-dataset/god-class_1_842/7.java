public void check(Parse cell, boolean value) {
    if (parseBoolean(cell) == value) {
        right(cell);
    } else {
        wrong(cell, "" + value);
    }
}
