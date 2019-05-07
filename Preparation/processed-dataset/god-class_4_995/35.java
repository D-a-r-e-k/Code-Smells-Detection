private String findAttachment(String linktext) {
    AttachmentManager mgr = m_engine.getAttachmentManager();
    Attachment att = null;
    try {
        att = mgr.getAttachmentInfo(m_context, linktext);
    } catch (ProviderException e) {
        log.warn("Finding attachments failed: ", e);
        return null;
    }
    if (att != null) {
        return att.getName();
    } else if (linktext.indexOf('/') != -1) {
        return linktext;
    }
    return null;
}
