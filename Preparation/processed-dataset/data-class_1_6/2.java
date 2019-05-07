public void doCell(Parse cell, int column) {
    try {
        if (column == 4) {
            cell.body = RenderedOutput();
        } else {
            super.doCell(cell, column);
        }
    } catch (Exception e) {
        exception(cell, e);
    }
}
