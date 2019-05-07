// Traversal //////////////////////////  
/* Altered by Rick Mugridge to dispatch on the first Fixture */
public void doTables(Parse tables) {
    summary.put("run date", new Date());
    summary.put("run elapsed time", new RunTime());
    if (tables != null) {
        Parse fixtureName = fixtureName(tables);
        if (fixtureName != null) {
            try {
                Fixture fixture = getLinkedFixtureWithArgs(tables);
                fixture.interpretTables(tables);
            } catch (Exception e) {
                exception(fixtureName, e);
                interpretFollowingTables(tables);
            }
        }
    }
}
