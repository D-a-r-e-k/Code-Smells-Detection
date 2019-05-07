private void syncMailboxInfo(MailboxStatus status) {
    boolean updated = false;
    if (status.getMessages() != -1 && messageFolderInfo.getExists() != status.getMessages()) {
        messageFolderInfo.setExists(status.getMessages());
        updated = true;
    }
    if (status.getMessages() == 0) {
        messageFolderInfo.setRecent(0);
        messageFolderInfo.setUnseen(0);
        updated = true;
    } else {
        if (status.getUnseen() != -1 && messageFolderInfo.getUnseen() != status.getUnseen()) {
            messageFolderInfo.setUnseen(status.getUnseen());
            updated = true;
        }
    }
    if (updated) {
        fireFolderPropertyChanged();
    }
}
