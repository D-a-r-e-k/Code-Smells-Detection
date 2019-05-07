public static TypeAdapter on(Fixture fixture, Field field) {
    TypeAdapter a = on(fixture, field.getType());
    a.target = fixture;
    a.field = field;
    return a;
}
