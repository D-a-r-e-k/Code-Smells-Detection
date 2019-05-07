public static TypeAdapter on(Fixture fixture, Method method) {
    TypeAdapter a = on(fixture, method.getReturnType());
    a.target = fixture;
    a.method = method;
    return a;
}
