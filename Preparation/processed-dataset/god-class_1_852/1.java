public void set(Object i) throws IllegalAccessException {
    field.setInt(target, ((Integer) i).intValue());
}
