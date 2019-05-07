protected TypeAdapter bindField(String name) throws Exception {
    return TypeAdapter.on(this, getTargetClass().getField(camel(name)));
}
