public void enter() throws Exception {
    Method method = method(1);
    Class type = method.getParameterTypes()[0];
    String text = cells.more.more.text();
    Object args[] = { TypeAdapter.on(actor, type).parse(text) };
    method.invoke(actor, args);
}
