/**
     *  Returns a page to which we shall redirect, based on the current value
     *  of the "captcha" parameter.
     *
     *  @param ctx WikiContext
     *  @return An URL to redirect to
     */
private String getRedirectPage(WikiContext ctx) {
    if (m_useCaptcha)
        return ctx.getURL(WikiContext.NONE, "Captcha.jsp", "page=" + ctx.getEngine().encodeName(ctx.getPage().getName()));
    return ctx.getURL(WikiContext.VIEW, m_errorPage);
}
