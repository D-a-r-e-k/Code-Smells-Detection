public void check(Parse cell, TypeAdapter a) {
    String text = cell.text();
    if (text.equals("")) {
        try {
            info(cell, a.toString(a.get()));
        } catch (Exception e) {
            info(cell, "error");
        }
    } else if (a == null) {
        ignore(cell);
    } else if (text.equals("error")) {
        try {
            Object result = a.invoke();
            wrong(cell, a.toString(result));
        } catch (IllegalAccessException e) {
            exception(cell, e);
        } catch (Exception e) {
            right(cell);
        }
    } else {
        try {
            Object result = a.get();
            if (a.equals(a.parse(text), result)) {
                right(cell);
            } else {
                wrong(cell, a.toString(result));
            }
        } catch (Exception e) {
            exception(cell, e);
        }
    }
}
