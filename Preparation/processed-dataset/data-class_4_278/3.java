public synchronized void cancelRendering(Scene sc) {
    Thread t = renderThread;
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
    RenderListener rl = listener;
    listener = null;
    if (rl != null)
        rl.renderingCanceled();
    finish();
}
