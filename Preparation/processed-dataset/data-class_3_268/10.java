protected void setMaximum(int progress) {
    if (getObservable() != null) {
        getObservable().setMax(progress);
    }
}
