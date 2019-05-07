/**
	 * 
	 */
private static SimpleFontFace createFontFace(String value) {
    SimpleFontFace fontFace = null;
    if (value != null) {
        if (value.trim().toUpperCase().endsWith(".TTF")) {
            fontFace = new SimpleFontFace(value);
        } else {
            JRFontUtil.checkAwtFont(value, JRProperties.getBooleanProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT));
            fontFace = new SimpleFontFace(new Font(value, Font.PLAIN, JRProperties.getIntegerProperty(JRFont.DEFAULT_FONT_SIZE)));
        }
    }
    return fontFace;
}
