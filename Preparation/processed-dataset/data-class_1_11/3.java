TypeAdapter type(String name) {
    Class type = name.equals("boolean") ? Boolean.class : name.equals("integer") ? Integer.class : name.equals("real") ? Float.class : name.equals("string") ? String.class : name.equals("date") ? Date.class : name.equals("money") ? Money.class : name.equals("scientific") ? ScientificDouble.class : name.equals("integers") ? IntegerArray.getClass() : name.equals("booleans") ? BooleanArray.getClass() : name.equals("strings") ? StringArray.getClass() : null;
    if (type == null)
        throw new RuntimeException("Unimplemented choice " + name);
    return TypeAdapter.on(this, type);
}
