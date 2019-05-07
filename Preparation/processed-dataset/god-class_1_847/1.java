// Factory //////////////////////////////////  
public static TypeAdapter on(Fixture target, Class type) {
    TypeAdapter a = adapterFor(type);
    a.init(target, type);
    return a;
}
