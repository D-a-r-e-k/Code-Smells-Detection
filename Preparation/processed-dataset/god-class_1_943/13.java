/** Interrupts the worker thread (action) that dialog is attached to */
public void interrupt() {
    this.worker.interrupt();
}
