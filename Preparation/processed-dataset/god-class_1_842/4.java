// answer comparisons ///////////////////////  
public void check(Parse cell, String value) {
    if (cell.text().equals(value)) {
        right(cell);
    } else {
        wrong(cell, value);
    }
}
