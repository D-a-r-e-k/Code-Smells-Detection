/* Added by Rick Mugridge */
private void interpretFollowingTables(Parse tables) {
    //listener.tableFinished(tables);  
    tables = tables.more;
    while (tables != null) {
        Parse fixtureName = fixtureName(tables);
        if (fixtureName != null) {
            try {
                Fixture fixture = getLinkedFixtureWithArgs(tables);
                fixture.doTable(tables);
            } catch (Throwable e) {
                exception(fixtureName, e);
            }
        }
        //listener.tableFinished(tables);  
        tables = tables.more;
    }
}
