/**
   * creates the loader thread.
   */
public LoadMessageThread createLoaderThread() {
    LoadMessageThread lmt = new LoadMessageThread(this);
    return lmt;
}
