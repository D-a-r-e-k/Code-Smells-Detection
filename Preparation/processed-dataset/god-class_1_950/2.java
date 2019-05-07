/**
	 * gets a list of all banned ip-address ranges
	 *
	 * @return list of banned ip-address ranges
	 * @throws SQLException in case of an database error
	 */
public List<InetAddressMask> getBannedAddresses() throws SQLException {
    DBTransaction transaction = TransactionPool.get().beginWork();
    try {
        List<InetAddressMask> res = getBannedAddresses(transaction);
        return res;
    } finally {
        TransactionPool.get().commit(transaction);
    }
}
