public static String tweak(String s) {
    int pos;
    if ((pos = s.toLowerCase().indexOf("e")) >= 0) {
        return tweak(s.substring(0, pos)) + s.substring(pos);
    }
    if (s.indexOf(".") >= 0) {
        return s + "5";
    }
    return s + ".5";
}
