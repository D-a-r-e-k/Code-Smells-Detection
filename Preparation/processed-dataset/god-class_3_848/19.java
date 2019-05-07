public void error(Parse cell, String message) {
    cell.body = escape(cell.text());
    cell.addToBody("<hr><pre>" + escape(message) + "</pre>");
    cell.addToTag(" bgcolor=\"" + yellow + "\"");
    counts.exceptions++;
}
