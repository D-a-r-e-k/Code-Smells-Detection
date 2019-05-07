/**
     * Don't call set autocommit(false) on connections obtained from the
     * DataSource. This can be helpfull in a few situations, such as if you
     * have a driver that complains if it is called when it is already off.
     * 
     * @param b
     */
public void setDontSetAutoCommitFalse(boolean b) {
    dontSetAutoCommitFalse = b;
}
