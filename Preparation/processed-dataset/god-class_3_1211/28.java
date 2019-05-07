/** Writes & syncs to the Directory dir, taking care to
   *  remove the segments file on exception
   *  <p>
   *  Note: {@link #changed()} should be called prior to this
   *  method if changes have been made to this {@link SegmentInfos} instance
   *  </p>  
   **/
final void commit(Directory dir) throws IOException {
    prepareCommit(dir);
    finishCommit(dir);
}
