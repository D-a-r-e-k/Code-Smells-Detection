public static String normalizeTag(String tag, Locale locale) {
    tag = Utilities.stripInvalidTagCharacters(tag);
    return locale == null ? tag.toLowerCase() : tag.toLowerCase(locale);
}
