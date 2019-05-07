/**
     * Gets the indentation on the left side.
     *
     * @return	a margin
     */
protected float indentLeft() {
    return left(indentation.indentLeft + indentation.listIndentLeft + indentation.imageIndentLeft + indentation.sectionIndentLeft);
}
