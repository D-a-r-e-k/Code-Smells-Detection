public void doRow(Parse row) {
    hasExecuted = false;
    try {
        reset();
        super.doRow(row);
        if (!hasExecuted) {
            execute();
        }
    } catch (Exception e) {
        exception(row.leaf(), e);
    }
}
