/**
   * Applies message filters to the new messages.
   */
public void runFilters(List proxies) throws MessagingException {
    if (isConnected()) {
        Folder current = getFolder();
        if (current != null && current.isOpen()) {
            int newCount = current.getNewMessageCount();
            if (newCount > 0) {
                int numProxies = proxies.size();
                List newProxies = new ArrayList();
                for (int i = 0; i < newCount; i++) {
                    newProxies.add(proxies.get((numProxies - newCount) + i));
                }
                proxies.removeAll(applyFilters(newProxies));
            }
        }
    }
}
