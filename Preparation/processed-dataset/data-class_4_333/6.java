public void hyperlinkUpdate(HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
        String[] path = e.getURL().getPath().split("/");
        if ("id".equals(path[1])) {
            select(path[2]);
        } else if ("action".equals(path[1])) {
            getFreeColClient().getActionManager().getFreeColAction(path[2]).actionPerformed(null);
        }
    }
}
