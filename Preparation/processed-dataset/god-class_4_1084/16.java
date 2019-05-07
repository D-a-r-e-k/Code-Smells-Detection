/**
     * Adds the current line to the list of lines and also adds an empty line.
     * @throws DocumentException on error
     */
protected void newLine() throws DocumentException {
    lastElementType = -1;
    carriageReturn();
    if (lines != null && !lines.isEmpty()) {
        lines.add(line);
        currentHeight += line.height();
    }
    line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
}
