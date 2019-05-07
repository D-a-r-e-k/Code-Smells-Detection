private void write(PrintWriter out, String text) throws IOException {
    while (text.indexOf("${") != -1) {
        text = substitute(text);
    }
    out.println(text);
}
