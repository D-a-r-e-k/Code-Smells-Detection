public int getCryptoMode() {
    if (decrypt == null)
        return -1;
    else
        return decrypt.getCryptoMode();
}
