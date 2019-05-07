public boolean execute(Database database) throws AxionException {
    setResultSet(executeQuery(database));
    return (getResultSet() != null);
}
