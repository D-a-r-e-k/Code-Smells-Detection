/* Added by Rick Mugridge to allow a dispatch into DoFixture */
protected void interpretTables(Parse tables) {
    try {
        // Don't create the first fixture again, because creation may do something important.  
        getArgsForTable(tables);
        // get them again for the new fixture object  
        doTable(tables);
    } catch (Exception ex) {
        exception(fixtureName(tables), ex);
        return;
    }
    interpretFollowingTables(tables);
}
