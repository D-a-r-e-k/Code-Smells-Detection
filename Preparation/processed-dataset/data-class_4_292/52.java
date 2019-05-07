public Object getConstantValueNoCheck(Session session) {
    try {
        return getValue(session);
    } catch (HsqlException e) {
        return null;
    }
}
