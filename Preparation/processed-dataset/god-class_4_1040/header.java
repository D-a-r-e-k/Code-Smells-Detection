void method0() { 
/** Query to retrieve Audit messages for a given user. */
public static final String QRY_GET_ALL_BY_PERIOD_BY_AGENCY_BY_USER = "org.webcurator.domain.model.audit.Audit.getAllByPeriodByAgencyByUser";
/** The database OID of the audit message */
private Long oid;
/** The date/time at which the event took place */
private Date dateTime;
/** The OID of the user that performed this action */
private Long userOid;
/** The OID of the agency that performed this action */
private Long agencyOid;
/** The username of the user that performed this action */
private String userName;
/** The first name of the user that performed this action */
private String firstname;
/** The last name ofthe user that performed this action */
private String lastname;
/** The action that was performed */
private String action;
/** The type of object this event acted on */
private String subjectType;
/** The OID of the object that was affected */
private Long subjectOid;
/** The message string to go with the audit log */
private String message;
}
