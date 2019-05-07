public void parseComplete() {
    final String tokenString = (context.negative) ? "-" + getText() : getText();
    final int length = tokenString.length();
    final char lastChar = tokenString.charAt(length - 1);
    final boolean floatLiteral = lastChar == 'f' || lastChar == 'F';
    type = floatLiteral ? Type.floatType : Type.doubleType;
    // JLS 15.8.1 
    value = floatLiteral ? (Object) Float.valueOf(tokenString.substring(0, length - 1)) : (Object) Double.valueOf(tokenString);
}
