/* Added from FitNesse*/
protected Fixture getLinkedFixtureWithArgs(Parse tables) throws Exception {
    Parse header = tables.at(0, 0, 0);
    Fixture fixture = loadFixture(header.text());
    fixture.counts = counts;
    fixture.summary = summary;
    fixture.getArgsForTable(tables);
    return fixture;
}
