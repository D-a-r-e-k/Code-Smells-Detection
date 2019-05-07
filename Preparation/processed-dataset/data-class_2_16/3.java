public void doCell(Parse cell, int column) {
    TypeAdapter a = columnBindings[column];
    try {
        String text = cell.text();
        if (text.equals("")) {
            check(cell, a);
        } else if (a == null) {
            ignore(cell);
        } else if (a.field != null) {
            a.set(a.parse(text));
        } else if (a.method != null) {
            check(cell, a);
        }
    } catch (Exception e) {
        exception(cell, e);
    }
}
