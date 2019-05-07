/**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
public void yypushback(int number) {
    if (number > yylength())
        zzScanError(ZZ_PUSHBACK_2BIG);
    zzMarkedPos -= number;
}
