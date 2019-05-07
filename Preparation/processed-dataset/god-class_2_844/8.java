protected TypeAdapter bindMethod(String name) throws Exception {
    return TypeAdapter.on(this, getTargetClass().getMethod(camel(name), new Class[] {}));
}
