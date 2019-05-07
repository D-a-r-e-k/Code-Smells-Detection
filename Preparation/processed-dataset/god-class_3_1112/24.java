public String strippedYytext() {
    String lineString = yytext();
    int len = lineString.length();
    len = len - ((len > 1 && lineString.charAt(len - 2) == '\r') ? 2 : 1);
    return (lineString.substring(0, len));
}
