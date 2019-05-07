private void checkThread() throws CanceledRequestException {
    if (Thread.currentThread().isInterrupted())
        throw new CanceledRequestException("ConnectionBuffer has been invalidated");
}
