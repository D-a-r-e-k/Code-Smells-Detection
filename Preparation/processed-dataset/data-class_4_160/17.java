/**
     *  Creates a simple text string describing the added content.
     *
     *  @param context
     *  @param newText
     *  @return Empty string, if there is no change.
     */
private static Change getChange(WikiContext context, String newText) {
    WikiPage page = context.getPage();
    StringBuffer change = new StringBuffer();
    WikiEngine engine = context.getEngine();
    // Get current page version 
    Change ch = new Change();
    try {
        String oldText = engine.getPureText(page.getName(), WikiProvider.LATEST_VERSION);
        String[] first = Diff.stringToArray(oldText);
        String[] second = Diff.stringToArray(newText);
        Revision rev = Diff.diff(first, second, new MyersDiff());
        if (rev == null || rev.size() == 0) {
            return ch;
        }
        for (int i = 0; i < rev.size(); i++) {
            Delta d = rev.getDelta(i);
            if (d instanceof AddDelta) {
                d.getRevised().toString(change, "", "\r\n");
                ch.m_adds++;
            } else if (d instanceof ChangeDelta) {
                d.getRevised().toString(change, "", "\r\n");
                ch.m_adds++;
            } else if (d instanceof DeleteDelta) {
                ch.m_removals++;
            }
        }
    } catch (DifferentiationFailedException e) {
        log.error("Diff failed", e);
    }
    // 
    //  Don't forget to include the change note, too 
    // 
    String changeNote = (String) page.getAttribute(WikiPage.CHANGENOTE);
    if (changeNote != null) {
        change.append("\r\n");
        change.append(changeNote);
    }
    // 
    //  And author as well 
    // 
    if (page.getAuthor() != null) {
        change.append("\r\n" + page.getAuthor());
    }
    ch.m_change = change.toString();
    return ch;
}
