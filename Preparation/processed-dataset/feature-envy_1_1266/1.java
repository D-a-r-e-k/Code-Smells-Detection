private static void gc() {
    try {
        System.gc();
        Thread.currentThread().sleep(100);
        System.runFinalization();
        Thread.currentThread().sleep(100);
        System.gc();
        Thread.currentThread().sleep(100);
        System.runFinalization();
        Thread.currentThread().sleep(100);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
