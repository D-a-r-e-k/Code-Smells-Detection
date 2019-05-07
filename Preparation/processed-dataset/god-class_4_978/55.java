/**
   * Do the final cleanup that must be done at the real end of the session(when closing the server if
   * running background server, or after killing the last window if no server is running).
   */
/* friendly*/
static void finalCleanupAndExit() {
    //currently it does almost nothing, but it's used. 
    System.exit(0);
}
