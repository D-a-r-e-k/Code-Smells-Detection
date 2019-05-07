public static Boolean overlaps(Session session, Object[] a, Type[] ta, Object[] b, Type[] tb) {
    if (a == null || b == null) {
        return null;
    }
    if (a[0] == null || b[0] == null) {
        return null;
    }
    if (a[1] == null) {
        a[1] = a[0];
    }
    if (b[1] == null) {
        b[1] = b[0];
    }
    Type commonType = ta[0].getCombinedType(tb[0], OpTypes.EQUAL);
    a[0] = commonType.castToType(session, a[0], ta[0]);
    b[0] = commonType.castToType(session, b[0], tb[0]);
    if (ta[1].isIntervalType()) {
        a[1] = commonType.add(a[0], a[1], ta[1]);
    } else {
        a[1] = commonType.castToType(session, a[1], ta[1]);
    }
    if (tb[1].isIntervalType()) {
        b[1] = commonType.add(b[0], b[1], tb[1]);
    } else {
        b[1] = commonType.castToType(session, b[1], tb[1]);
    }
    if (commonType.compare(session, a[0], a[1]) > 0) {
        Object temp = a[0];
        a[0] = a[1];
        a[1] = temp;
    }
    if (commonType.compare(session, b[0], b[1]) > 0) {
        Object temp = b[0];
        b[0] = b[1];
        b[1] = temp;
    }
    if (commonType.compare(session, a[0], b[0]) > 0) {
        Object[] temp = a;
        a = b;
        b = temp;
    }
    if (commonType.compare(session, a[1], b[0]) > 0) {
        return Boolean.TRUE;
    }
    return Boolean.FALSE;
}
