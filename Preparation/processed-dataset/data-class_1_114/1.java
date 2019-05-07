/**
	 * gets a list of all banned ip-address ranges
	 *
	 * @param transaction DBTransaction
	 * @return list of banned ip-address ranges
	 * @throws SQLException in case of an database error
	 */
public List<InetAddressMask> getBannedAddresses(DBTransaction transaction) throws SQLException {
    List<InetAddressMask> permanentBans = new LinkedList<InetAddressMask>();
    /* read ban list from DB */
    String query = "select address, mask from banlist";
    logger.debug("getBannedAddresses is executing query " + query);
    ResultSet resultSet = transaction.query(query, null);
    permanentBans.clear();
    while (resultSet.next()) {
        String address = resultSet.getString("address");
        String mask = resultSet.getString("mask");
        InetAddressMask iam = new InetAddressMask(address, mask);
        permanentBans.add(iam);
    }
    resultSet.close();
    return permanentBans;
}
