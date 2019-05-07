// code smell note: copied from DocumentParseFixture	  
private String GenerateOutput(Parse document) throws ParseException {
    StringWriter result = new StringWriter();
    document.print(new PrintWriter(result));
    return result.toString().trim();
}
