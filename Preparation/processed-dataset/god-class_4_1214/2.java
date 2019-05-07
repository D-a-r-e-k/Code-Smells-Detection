/**
     * Called by the cluster before sending it to the other
     * nodes.
     *
     * @param member Member
     */
public void setAddress(Member member) {
    this.address = member;
}
