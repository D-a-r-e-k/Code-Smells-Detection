public boolean existsParent(Session session, PersistentStore store, Object[] rowdata, int[] rowColMap) {
    NodeAVL node = findNode(session, store, rowdata, rowColMap, rowColMap.length, OpTypes.EQUAL, TransactionManager.ACTION_REF, false);
    return node != null;
}
