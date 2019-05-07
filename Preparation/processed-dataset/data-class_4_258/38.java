/**
     * Sets the font name that will appear in the pdf font dictionary.
     * Use with care as it can easily make a font unreadable if not embedded.
     * @param name the new font name
     */
@Override
public void setPostscriptFontName(String name) {
    fontName = name;
}
