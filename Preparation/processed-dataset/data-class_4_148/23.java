/**
     * Whether or not to obtain locks when inserting new jobs/triggers.  
     * Defaults to <code>true</code>, which is safest - some db's (such as 
     * MS SQLServer) seem to require this to avoid deadlocks under high load,
     * while others seem to do fine without.  
     * 
     * <p>Setting this property to <code>false</code> will provide a 
     * significant performance increase during the addition of new jobs 
     * and triggers.</p>
     * 
     * @param lockOnInsert
     */
public void setLockOnInsert(boolean lockOnInsert) {
    this.lockOnInsert = lockOnInsert;
}
