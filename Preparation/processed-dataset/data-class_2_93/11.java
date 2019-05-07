public void forceNotify() {
    if (toNotify == null)
        return;
    synchronized (toNotify) {
        toNotify.notify();
        toNotify = null;
    }
}
