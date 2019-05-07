/**
     * Set the signature flags.
     * @param f the flags. This flags are ORed with current ones
     */
@Override
public void setSigFlags(int f) {
    sigFlags |= f;
}
