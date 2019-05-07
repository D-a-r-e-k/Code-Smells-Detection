/**
     * If true (and <code>OverWriteExistingData</code> is false) then any 
     * job/triggers encountered in this file that have names that already exist 
     * in the scheduler will be ignored, and no error will be produced.
     * 
     * @see #isOverWriteExistingData()
     */
public boolean isIgnoreDuplicates() {
    return ignoreDuplicates;
}
