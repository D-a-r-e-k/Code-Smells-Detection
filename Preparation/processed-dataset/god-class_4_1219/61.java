/*
     * (non-Javadoc)
     * @see megamek.common.Entity#canTransferCriticals(int)
     */
@Override
public boolean canTransferCriticals(int loc) {
    // BAs can never transfer crits  
    return false;
}
