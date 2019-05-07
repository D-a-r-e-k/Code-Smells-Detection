FieldMethodZero(String name, RubyModule host, Field field) {
    super(host, Visibility.PUBLIC);
    if (!Ruby.isSecurityRestricted()) {
        field.setAccessible(true);
    }
    this.field = field;
    this.name = name;
}
