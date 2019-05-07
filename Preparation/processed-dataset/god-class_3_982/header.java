void method0() { 
private static final long serialVersionUID = 12423409423L;
protected static final int SECOND = 0;
protected static final int MINUTE = 1;
protected static final int HOUR = 2;
protected static final int DAY_OF_MONTH = 3;
protected static final int MONTH = 4;
protected static final int DAY_OF_WEEK = 5;
protected static final int YEAR = 6;
protected static final int ALL_SPEC_INT = 99;
// '*' 
protected static final int NO_SPEC_INT = 98;
// '?' 
protected static final Integer ALL_SPEC = new Integer(ALL_SPEC_INT);
protected static final Integer NO_SPEC = new Integer(NO_SPEC_INT);
protected static final Map monthMap = new HashMap(20);
protected static final Map dayMap = new HashMap(60);
private String cronExpression = null;
private TimeZone timeZone = null;
protected transient TreeSet seconds;
protected transient TreeSet minutes;
protected transient TreeSet hours;
protected transient TreeSet daysOfMonth;
protected transient TreeSet months;
protected transient TreeSet daysOfWeek;
protected transient TreeSet years;
protected transient boolean lastdayOfWeek = false;
protected transient int nthdayOfWeek = 0;
protected transient boolean lastdayOfMonth = false;
protected transient boolean nearestWeekday = false;
protected transient boolean expressionParsed = false;
}
