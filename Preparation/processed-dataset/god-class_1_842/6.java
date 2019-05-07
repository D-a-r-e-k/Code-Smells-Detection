public void check(Parse cell, double value) {
    if (parseDouble(cell) == value) {
        right(cell);
    } else {
        wrong(cell, Double.toString(value));
    }
}
