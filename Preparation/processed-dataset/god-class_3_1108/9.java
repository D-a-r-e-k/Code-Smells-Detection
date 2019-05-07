/**
   * Create an exact duplicate of this record.
   */
public KeystrokeRecord duplicate() {
    return new KeystrokeRecord(keyCode, modifiers, name, script);
}
