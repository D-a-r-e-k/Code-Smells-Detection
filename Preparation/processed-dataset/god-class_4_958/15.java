void setValueMnemonic(final String mnemonic) {
    if (mnemonic == null)
        throw new IllegalArgumentException("null input: mnemonic");
    m_valueMnemonic = mnemonic;
}
