// Utility //////////////////////////////////  
protected void bind(Parse heads) {
    columnBindings = new TypeAdapter[heads.size()];
    for (int i = 0; heads != null; i++, heads = heads.more) {
        String name = heads.text();
        String suffix = "()";
        try {
            if (name.equals("")) {
                columnBindings[i] = null;
            } else if (name.endsWith(suffix)) {
                columnBindings[i] = bindMethod(name.substring(0, name.length() - suffix.length()));
            } else {
                columnBindings[i] = bindField(name);
            }
        } catch (Exception e) {
            exception(heads, e);
        }
    }
}
