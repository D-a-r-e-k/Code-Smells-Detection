public void wrong(Parse cell) {
    cell.addToTag(" bgcolor=\"" + red + "\"");
    cell.body = escape(cell.text());
    counts.wrong++;
}
