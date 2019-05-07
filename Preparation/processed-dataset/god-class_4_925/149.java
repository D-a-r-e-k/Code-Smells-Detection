/**
	 * Makes QSObjectPool from ObjectPool
	 * @since 1.4.5
	 */
private QSObjectPool makeQSObjectPool(ObjectPool objectPool) throws Exception {
    return (QSObjectPool) qsObjectPoolMaker.getQSObjectPool(objectPool);
}
