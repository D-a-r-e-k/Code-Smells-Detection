public boolean owns(Ownable ownable) {
    if (ownable == null)
        return false;
    return this.equals(ownable.getOwner());
}
