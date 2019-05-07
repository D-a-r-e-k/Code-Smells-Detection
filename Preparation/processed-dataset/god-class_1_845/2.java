public static double precision(String s) {
    double value = Double.parseDouble(s);
    double bound = Double.parseDouble(tweak(s.trim()));
    return Math.abs(bound - value);
}
