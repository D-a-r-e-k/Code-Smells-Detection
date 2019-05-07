/**
     *  This method checks if the hash value is still valid, i.e. if it exists at all. This
     *  can occur in two cases: either this is a spam bot which is not adaptive, or it is
     *  someone who has been editing one page for too long, and their session has expired.
     *  <p>
     *  This method puts a redirect to the http response field to page "SessionExpired"
     *  and logs the incident in the spam log (it may or may not be spam, but it's rather likely
     *  that it is).
     *
     *  @param context The WikiContext
     *  @param pageContext The JSP PageContext.
     *  @return True, if hash is okay.  False, if hash is not okay, and you need to redirect.
     *  @throws IOException If redirection fails
     *  @since 2.6
     */
public static final boolean checkHash(WikiContext context, PageContext pageContext) throws IOException {
    String hashName = getHashFieldName((HttpServletRequest) pageContext.getRequest());
    if (pageContext.getRequest().getParameter(hashName) == null) {
        if (pageContext.getAttribute(hashName) == null) {
            Change change = getChange(context, EditorManager.getEditedText(pageContext));
            log(context, REJECT, "MissingHash", change.m_change);
            String redirect = context.getURL(WikiContext.VIEW, "SessionExpired");
            ((HttpServletResponse) pageContext.getResponse()).sendRedirect(redirect);
            return false;
        }
    }
    return true;
}
