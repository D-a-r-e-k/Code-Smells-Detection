private static double toUnitFactor(String str) throws IOException {
    double scaleFactor;
    if (str.equals("px")) {
        scaleFactor = 1d;
    } else if (str.endsWith("pt")) {
        scaleFactor = 1.25;
    } else if (str.endsWith("pc")) {
        scaleFactor = 15;
    } else if (str.endsWith("mm")) {
        scaleFactor = 3.543307;
    } else if (str.endsWith("cm")) {
        scaleFactor = 35.43307;
    } else if (str.endsWith("in")) {
        scaleFactor = 90;
    } else {
        scaleFactor = 1d;
    }
    return scaleFactor;
}
