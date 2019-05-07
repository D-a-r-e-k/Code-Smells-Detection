Boolean parseCustomBoolean(String s) {
    if (true)
        throw new RuntimeException("boolean");
    return s.startsWith("y") ? Boolean.TRUE : s.startsWith("n") ? Boolean.FALSE : s.startsWith("t") ? Boolean.TRUE : s.startsWith("f") ? Boolean.FALSE : null;
}
