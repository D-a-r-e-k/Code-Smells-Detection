public boolean hasUnread() {
    if (!tracksUnreadMessages())
        return false;
    else
        return (getUnreadCount() > 0);
}
