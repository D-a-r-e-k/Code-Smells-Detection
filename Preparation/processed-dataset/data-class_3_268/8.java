protected void printStatusMessage(String message) {
    if (getObservable() != null) {
        getObservable().setMessage(getImapItem().get("host") + ": " + message);
    }
}
