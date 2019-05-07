/**
	 * 
	 */
public String getExportFont(String key) {
    String exportFont = exportFonts == null ? null : (String) exportFonts.get(key);
    return exportFont == null ? defaultExportFont : exportFont;
}
