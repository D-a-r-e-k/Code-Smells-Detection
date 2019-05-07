protected Object safeConvert(IRubyObject value) {
    Object newValue = value.toJava(Object.class);
    if (newValue == null) {
        if (field.getType().isPrimitive()) {
            throw value.getRuntime().newTypeError("wrong type for " + field.getType().getName() + ": null");
        }
    } else if (!field.getType().isInstance(newValue)) {
        throw value.getRuntime().newTypeError("wrong type for " + field.getType().getName() + ": " + newValue.getClass().getName());
    }
    return newValue;
}
