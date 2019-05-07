// Trims only the end 
public void pushbackTrim() {
    String lineString = yytext();
    int len = lineString.length();
    yypushback((len > 1 && lineString.charAt(len - 2) == '\r') ? 2 : 1);
}
