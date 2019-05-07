public void exception(Parse cell, Throwable exception) {
    while (exception.getClass().equals(InvocationTargetException.class)) {
        exception = ((InvocationTargetException) exception).getTargetException();
    }
    final StringWriter buf = new StringWriter();
    exception.printStackTrace(new PrintWriter(buf));
    error(cell, buf.toString());
}
