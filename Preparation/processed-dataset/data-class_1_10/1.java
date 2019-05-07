public String Text() {
    HTML = HTML.replaceAll("\\\\u00a0", " ");
    return escapeAscii(Parse.htmlToText(HTML));
}
