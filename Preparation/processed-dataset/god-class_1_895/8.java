public void removeOther() {
    Account a = (Account) getCategory();
    a.getEntries().removeElement(getOther());
}
