/** Call this to start a commit.  This writes the new
   *  segments file, but writes an invalid checksum at the
   *  end, so that it is not visible to readers.  Once this
   *  is called you must call {@link #finishCommit} to complete
   *  the commit or {@link #rollbackCommit} to abort it.
   *  <p>
   *  Note: {@link #changed()} should be called prior to this
   *  method if changes have been made to this {@link SegmentInfos} instance
   *  </p>  
   **/
final void prepareCommit(Directory dir) throws IOException {
    if (pendingSegnOutput != null) {
        throw new IllegalStateException("prepareCommit was already called");
    }
    write(dir);
}
