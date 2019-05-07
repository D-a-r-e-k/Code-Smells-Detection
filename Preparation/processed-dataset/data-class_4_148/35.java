/**
     * <p>
     * set the SQL statement to use to select and lock a row in the "locks"
     * table.
     * </p>
     * 
     * @see StdRowLockSemaphore
     */
public void setSelectWithLockSQL(String string) {
    selectWithLockSQL = string;
}
