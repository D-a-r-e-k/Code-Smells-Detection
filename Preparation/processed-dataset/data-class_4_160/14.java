/**
     *  If the spam filter notices changes in the black list page, it will refresh
     *  them automatically.
     *
     *  @param context
     */
private void refreshBlacklists(WikiContext context) {
    try {
        WikiPage source = context.getEngine().getPage(m_forbiddenWordsPage);
        Attachment att = context.getEngine().getAttachmentManager().getAttachmentInfo(context, m_blacklist);
        boolean rebuild = false;
        // 
        //  Rebuild, if the page or the attachment has changed since. 
        // 
        if (source != null) {
            if (m_spamPatterns == null || m_spamPatterns.isEmpty() || source.getLastModified().after(m_lastRebuild)) {
                rebuild = true;
            }
        }
        if (att != null) {
            if (m_spamPatterns == null || m_spamPatterns.isEmpty() || att.getLastModified().after(m_lastRebuild)) {
                rebuild = true;
            }
        }
        // 
        //  Do the actual rebuilding.  For simplicity's sake, we always rebuild the complete 
        //  filter list regardless of what changed. 
        // 
        if (rebuild) {
            m_lastRebuild = new Date();
            m_spamPatterns = parseWordList(source, (source != null) ? (String) source.getAttribute(LISTVAR) : null);
            log.info("Spam filter reloaded - recognizing " + m_spamPatterns.size() + " patterns from page " + m_forbiddenWordsPage);
            if (att != null) {
                InputStream in = context.getEngine().getAttachmentManager().getAttachmentStream(att);
                StringWriter out = new StringWriter();
                FileUtil.copyContents(new InputStreamReader(in, "UTF-8"), out);
                Collection<Pattern> blackList = parseBlacklist(out.toString());
                log.info("...recognizing additional " + blackList.size() + " patterns from blacklist " + m_blacklist);
                m_spamPatterns.addAll(blackList);
            }
        }
    } catch (IOException ex) {
        log.info("Unable to read attachment data, continuing...", ex);
    } catch (ProviderException ex) {
        log.info("Failed to read spam filter attachment, continuing...", ex);
    }
}
