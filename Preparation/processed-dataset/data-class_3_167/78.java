protected UndoableEdit[] augment(UndoableEdit[] e, UndoableEdit edit) {
    if (edit != null) {
        int size = (e != null) ? e.length + 1 : 1;
        UndoableEdit[] result = new UndoableEdit[size];
        if (e != null)
            System.arraycopy(e, 0, result, 0, size - 2);
        result[size - 1] = edit;
        return result;
    }
    return e;
}
