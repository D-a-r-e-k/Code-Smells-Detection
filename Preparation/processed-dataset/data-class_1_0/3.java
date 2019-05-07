private String GenerateOutput(Parse parse) {
    StringWriter result = new StringWriter();
    parse.print(new PrintWriter(result));
    return result.toString();
}
