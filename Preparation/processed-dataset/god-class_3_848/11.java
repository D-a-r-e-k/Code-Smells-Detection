public void doCells(Parse cells) {
    for (int i = 0; cells != null; i++) {
        try {
            doCell(cells, i);
        } catch (Exception e) {
            exception(cells, e);
        }
        cells = cells.more;
    }
}
