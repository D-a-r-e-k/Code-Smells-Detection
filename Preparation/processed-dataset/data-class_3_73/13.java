private static String genjj_3Call(Expansion e) {
    if (e.internal_name.startsWith("jj_scan_token"))
        return e.internal_name;
    else
        return "jj_3" + e.internal_name + "()";
}
