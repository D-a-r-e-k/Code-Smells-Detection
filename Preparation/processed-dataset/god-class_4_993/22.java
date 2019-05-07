/**
     *  This method is used to calculate an unique code when submitting the page
     *  to detect edit conflicts.  It currently incorporates the last-modified
     *  date of the page, and the IP address of the submitter.
     *
     *  @param page The WikiPage under edit
     *  @param request The HTTP Request
     *  @since 2.6
     *  @return A hash value for this page and session
     */
public static final String getSpamHash(WikiPage page, HttpServletRequest request) {
    long lastModified = 0;
    if (page.getLastModified() != null)
        lastModified = page.getLastModified().getTime();
    long remote = request.getRemoteAddr().hashCode();
    return Long.toString(lastModified ^ remote);
}
