void method0() { 
/** Query identifier for listing all records */
public static final String QRY_GET_ALL_BY_USER_BY_AGENCY_BY_TYPE = "org.webcurator.domain.model.report.AbstractTargetScheduleView.getAllByUserByAgencyByType";
public static final String QRY_GET_SUMMARY_STATS_BY_AGENCY = "org.webcurator.domain.model.report.AbstractTargetScheduleView.getSummaryStatsByAgency";
/** The composite primary key. The abstract_target oid and the schedule oid strung together with a comma */
private String theKey;
///** The database OID of the record */  
//private Long oid;  
/** 
     * Identifies whether this is a Target or Group.
     */
private String objectTypeDesc;
/** The target (or group) name. */
private String name;
/** The state of the target (or group) */
private int state;
/** The target (or group) owner name. */
private String ownerName;
/** The target (or group) owning agency name. */
private String agencyName;
/** The oid of the schedule record. */
private Long scheduleOid;
/** The schedule start date*/
private Date scheduleStartDate;
/** The schedule end date*/
private Date scheduleEndDate;
/** Type Identifier for schedules. */
private int scheduleType;
/** The schedule cron pattern*/
private String scheduleCronPattern;
}
