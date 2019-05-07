public int getUnreadCount() {
    if (!tracksUnreadMessages())
        return 0;
    else
        return unreadCount;
}
