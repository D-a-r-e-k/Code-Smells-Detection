void addJavaScript(PdfAction js) {
    if (js.get(PdfName.JS) == null)
        throw new RuntimeException(MessageLocalization.getComposedMessage("only.javascript.actions.are.allowed"));
    try {
        documentLevelJS.put(SIXTEEN_DIGITS.format(jsCounter++), writer.addToBody(js).getIndirectReference());
    } catch (IOException e) {
        throw new ExceptionConverter(e);
    }
}
