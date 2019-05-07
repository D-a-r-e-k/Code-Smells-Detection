protected long getMisfireTime() {
    long misfireTime = System.currentTimeMillis();
    if (getMisfireThreshold() > 0) {
        misfireTime -= getMisfireThreshold();
    }
    return (misfireTime > 0) ? misfireTime : 0;
}
