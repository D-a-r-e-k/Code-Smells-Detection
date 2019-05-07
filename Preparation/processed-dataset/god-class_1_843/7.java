protected void exit() {
    output.close();
    System.err.println(fixture.counts());
    System.exit(fixture.counts.wrong + fixture.counts.exceptions);
}
