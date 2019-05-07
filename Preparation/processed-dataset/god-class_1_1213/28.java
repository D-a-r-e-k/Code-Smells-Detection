public long getCurrentTimeMillis() {
    long systime = System.currentTimeMillis();
    if ((systime - currentTimeMillis) > 1000) {
        currentTimeMillis = new java.util.Date(systime).getTime();
    }
    return currentTimeMillis;
}
