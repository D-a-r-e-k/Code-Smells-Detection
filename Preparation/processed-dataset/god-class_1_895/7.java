public void remove() {
    Account to = (Account) getCategory();
    Account from = (Account) getOther().getCategory();
    to.getEntries().removeElement(getOther());
    from.getEntries().removeElement(this);
}
