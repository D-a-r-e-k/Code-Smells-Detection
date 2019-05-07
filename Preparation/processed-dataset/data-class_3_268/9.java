protected void setProgress(int progress) {
    if (getObservable() != null) {
        getObservable().setCurrent(progress);
    }
}
