public String FixtureName() throws Exception {
    Parse tableParse = GenerateTableParse(Table);
    String result = fixtureName(tableParse).text();
    if (result.equals(""))
        return "(missing)";
    return result;
}
