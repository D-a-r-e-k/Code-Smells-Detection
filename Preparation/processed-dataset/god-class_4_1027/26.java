public boolean handleIdle(boolean isIdle) {
    if (isIdle) {
        if (Server.getServer().getServerConfiguration().isDisableTimeout()) {
            acknowledgeIdle(true);
            return true;
        } else if (mHandleTimeout) {
            acknowledgeIdle(true);
            return true;
        }
    }
    return super.handleIdle(isIdle);
}
