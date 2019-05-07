/**
   * Closes the input stream.
   */
public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;
    /* indicate end of file */
    zzEndRead = zzStartRead;
    /* invalidate buffer    */
    if (zzReader != null)
        zzReader.close();
}
