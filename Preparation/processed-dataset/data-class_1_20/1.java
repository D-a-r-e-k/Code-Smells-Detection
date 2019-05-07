public String toString() {
    elapsed = (System.currentTimeMillis() - start);
    if (elapsed > 600000) {
        return d(3600000) + ":" + d(600000) + d(60000) + ":" + d(10000) + d(1000);
    } else {
        return d(60000) + ":" + d(10000) + d(1000) + "." + d(100) + d(10);
    }
}
