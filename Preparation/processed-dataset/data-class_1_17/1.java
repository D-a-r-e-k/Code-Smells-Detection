public static ScientificDouble valueOf(String s) {
    ScientificDouble result = new ScientificDouble(Double.parseDouble(s));
    result.precision = precision(s);
    return result;
}
