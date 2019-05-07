public void check() throws Exception {
    TypeAdapter adapter = TypeAdapter.on(actor, method(0));
    check(cells.more.more, adapter);
}
