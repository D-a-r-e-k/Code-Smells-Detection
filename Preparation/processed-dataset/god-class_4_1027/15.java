public boolean handleCallback(HmeEvent event) {
    for (int i = 0; i < mCallbacks.size(); i++) {
        Callback callback = (Callback) mCallbacks.get(i);
        if (callback.handleEvent(event)) {
            mCallbacks.remove(i);
            return true;
        }
    }
    return false;
}
