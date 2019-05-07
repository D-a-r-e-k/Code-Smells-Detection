public void process() {
    try {
        if (input.indexOf("<wiki>") >= 0) {
            tables = new Parse(input, new String[] { "wiki", "table", "tr", "td" });
            fixture.doTables(tables.parts);
        } else {
            tables = new Parse(input, new String[] { "table", "tr", "td" });
            fixture.doTables(tables);
        }
    } catch (Exception e) {
        exception(e);
    }
    tables.print(output);
}
