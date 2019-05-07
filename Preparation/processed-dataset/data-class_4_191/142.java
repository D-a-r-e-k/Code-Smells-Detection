/**
   * sets the status.
   */
public void setStatus(int newStatus) {
    synchronized (this) {
        status = newStatus;
    }
}
