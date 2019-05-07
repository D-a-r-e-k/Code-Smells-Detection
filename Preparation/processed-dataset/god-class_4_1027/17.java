public void setTracker(Tracker tracker) {
    getPlayer().setTracker(tracker);
    List list = tracker.getList();
    if (list.size() > 0) {
        Item nameFile = (Item) list.get(0);
        if (nameFile != null && nameFile.isFile()) {
            File file = (File) nameFile.getValue();
        }
    }
}
