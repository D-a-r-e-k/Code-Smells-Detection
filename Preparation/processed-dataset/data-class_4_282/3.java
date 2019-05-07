public synchronized void cancelRendering(Scene sc) {
    Thread t = renderThread;
    RenderListener rl = listener;
    if (theScene != sc)
        return;
    renderThread = null;
    if (t == null)
        return;
    try {
        while (t.isAlive()) {
            Thread.sleep(100);
        }
    } catch (InterruptedException ex) {
    }
    finish(null);
    rl.renderingCanceled();
}
