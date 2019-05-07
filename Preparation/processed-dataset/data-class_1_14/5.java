protected void mark(Parse row) {
    // mark summary good/bad without counting beyond here  
    Counts official = counts;
    counts = new Counts();
    Parse cell = row.parts.more;
    if (official.wrong + official.exceptions > 0) {
        wrong(cell);
    } else {
        right(cell);
    }
    counts = official;
}
