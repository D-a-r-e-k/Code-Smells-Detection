private Boolean testAllAnyCondition(Session session, Object[] o) {
    SubQuery subquery = nodes[RIGHT].subQuery;
    subquery.materialiseCorrelated(session);
    Boolean result = getAllAnyValue(session, o, subquery);
    return result;
}
