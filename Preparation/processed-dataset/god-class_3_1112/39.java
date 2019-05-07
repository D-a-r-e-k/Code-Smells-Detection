/**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
        zzEOFDone = true;
        yyclose();
    }
}
