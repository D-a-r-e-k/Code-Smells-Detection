public void materialise(Session session) {
    if (subQuery == null) {
        return;
    }
    if (subQuery.isCorrelated()) {
        subQuery.materialiseCorrelated(session);
    } else {
        subQuery.materialise(session);
    }
}
