public void doCell(Parse cell, int col) {
    try {
        char head = heads.at(col).text().charAt(0);
        switch(head) {
            case 't':
                type = type(cell.text());
                break;
            case 'x':
                x = type.parse(cell.text());
                break;
            case 'y':
                y = type.parse(cell.text());
                break;
            case '=':
                check(cell, type.equals(x, y));
                break;
            case '?':
                cell.addToBody(info("x: " + print(x) + " y: " + print(y)));
                break;
            default:
                throw new Exception("don't do " + head);
        }
    } catch (Exception e) {
        exception(cell, e);
    }
}
