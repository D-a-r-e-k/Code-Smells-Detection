// code smell note: copied from ParseFixture	  
private String GenerateOutput(Parse parse) {
    StringWriter result = new StringWriter();
    parse.print(new PrintWriter(result));
    return result.toString().trim();
}
