/**
     * Sets the memo of both entries.
     */
public void setMemo(String aMemo) {
    if (memo != null && memo.equals(aMemo))
        return;
    memo = aMemo.length() == 0 ? null : aMemo;
    changeSupport.firePropertyChange("memo", null, memo);
    if (other == null)
        return;
    other.memo = memo;
    other.changeSupport.firePropertyChange("memo", null, memo);
}
