static String getSubsetPrefix(PdfDictionary dic) {
    if (dic == null)
        return null;
    String s = getFontName(dic);
    if (s == null)
        return null;
    if (s.length() < 8 || s.charAt(6) != '+')
        return null;
    for (int k = 0; k < 6; ++k) {
        char c = s.charAt(k);
        if (c < 'A' || c > 'Z')
            return null;
    }
    return s;
}
