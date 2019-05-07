/**
     * Get the regular expression URLs must match.
     *
     * @return regular expression (or empty) string
     */
public String getEmbeddedUrlRE() {
    return getPropertyAsString(EMBEDDED_URL_RE, "");
}
