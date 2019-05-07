public boolean isMetadataEncrypted() {
    if (decrypt == null)
        return false;
    else
        return decrypt.isMetadataEncrypted();
}
