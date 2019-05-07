/**
     * Checks if the document was opened with the owner password so that the end application
     * can decide what level of access restrictions to apply. If the document is not encrypted
     * it will return <CODE>true</CODE>.
     * @return <CODE>true</CODE> if the document was opened with the owner password or if it's not encrypted,
     * <CODE>false</CODE> if the document was opened with the user password
     */
public final boolean isOpenedWithFullPermissions() {
    return !encrypted || ownerPasswordUsed || unethicalreading;
}
