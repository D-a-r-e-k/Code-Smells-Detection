/**
	 * 
	 */
public boolean supportsLocale(Locale locale) {
    return locales == null || locales.contains(JRDataUtils.getLocaleCode(locale));
}
