private Boolean testExistsCondition(Session session) {
    nodes[LEFT].materialise(session);
    return nodes[LEFT].getTable().isEmpty(session) ? Boolean.FALSE : Boolean.TRUE;
}
