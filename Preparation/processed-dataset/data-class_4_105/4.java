private void checkInterruptStatus() throws InterruptedException {
    if (Thread.interrupted()) {
        throw new InterruptedException();
    }
}
