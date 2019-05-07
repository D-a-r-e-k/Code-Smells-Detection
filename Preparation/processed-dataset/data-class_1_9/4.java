String color(Parse cell) {
    String b = extract(cell.tag, "bgcolor=\"", "white");
    String f = extract(cell.body, "<font color=", "black");
    return f.equals("black") ? b : f + "/" + b;
}
