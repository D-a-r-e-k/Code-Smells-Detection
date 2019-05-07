// Traversal ////////////////////////////////  
public void doCells(Parse cells) {
    this.cells = cells;
    try {
        Method action = getClass().getMethod(cells.text(), empty);
        action.invoke(this, empty);
    } catch (Exception e) {
        exception(cells, e);
    }
}
