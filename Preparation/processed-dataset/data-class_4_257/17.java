/**
     * If the current line is not empty or null, it is added to the arraylist
     * of lines and a new empty line is added.
     */
protected void carriageReturn() {
    // the arraylist with lines may not be null 
    if (lines == null) {
        lines = new ArrayList<PdfLine>();
    }
    // If the current line is not null 
    if (line != null) {
        // we check if the end of the page is reached (bugfix by Francois Gravel) 
        if (currentHeight + line.height() + leading < indentTop() - indentBottom()) {
            // if so nonempty lines are added and the height is augmented 
            if (line.size() > 0) {
                currentHeight += line.height();
                lines.add(line);
                pageEmpty = false;
            }
        } else {
            newPage();
        }
    }
    if (imageEnd > -1 && currentHeight > imageEnd) {
        imageEnd = -1;
        indentation.imageIndentRight = 0;
        indentation.imageIndentLeft = 0;
    }
    // a new current line is constructed 
    line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
}
