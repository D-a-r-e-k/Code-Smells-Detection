protected void exception(Exception e) {
    tables = new Parse("body", "Unable to parse input. Input ignored.", null, null);
    fixture.exception(tables, e);
}
