public void wrong(Parse cell, String actual) {
    wrong(cell);
    cell.addToBody(label("expected") + "<hr>" + escape(actual) + label("actual"));
}
