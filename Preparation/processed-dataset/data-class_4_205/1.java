/**
	 * Standard constructor for WCT usage.
	 * @param aUrlPattern The UrlPattern.
	 * @param aPermission The CutdownPermission.
	//public AbstractTargetScheduleView(UrlPattern aUrlPattern, CutdownPermission aPermission) {
	//	urlPattern = aUrlPattern;
	//	permission = aPermission;
	//	
	//	domain = HierarchicalPermissionMappingStrategy.calculateDomain(aUrlPattern.getPattern());
	}
	 */
/**
     * Get the primary key of the AbstractTargetScheduleView record.
     * @return Returns the key.
     * @hibernate.id column="THEKEY" generator-class="native"
     */
public String getTheKey() {
    return theKey;
}
