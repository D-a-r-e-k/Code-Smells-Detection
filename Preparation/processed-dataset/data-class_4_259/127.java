public byte[] computeUserPassword() {
    if (!encrypted || !ownerPasswordUsed)
        return null;
    return decrypt.computeUserPassword(password);
}
