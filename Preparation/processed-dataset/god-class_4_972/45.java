/******************************************************************************/
private synchronized void stop(double sec) {
    try {
        wait((long) (sec * 1000.0));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
