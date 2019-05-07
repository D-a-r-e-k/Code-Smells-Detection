private String stripDelimiters(String s) {
    return s.replaceAll("^\\[", "").replaceAll("]$", "");
}
