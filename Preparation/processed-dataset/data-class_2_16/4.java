public void check(Parse cell, TypeAdapter a) {
    if (!hasExecuted) {
        try {
            execute();
        } catch (Exception e) {
            exception(cell, e);
        }
        hasExecuted = true;
    }
    super.check(cell, a);
}
