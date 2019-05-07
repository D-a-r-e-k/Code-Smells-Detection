void method0() { 
/**
     *
     */
private static final long serialVersionUID = -857210851169206264L;
protected boolean m_bHasNoTurret = false;
protected boolean m_bTurretLocked = false;
protected boolean m_bTurretJammed = false;
protected boolean m_bTurretEverJammed = false;
private int m_nTurretOffset = 0;
private int m_nStunnedTurns = 0;
private boolean m_bImmobile = false;
private boolean m_bImmobileHit = false;
private int burningLocations = 0;
protected int movementDamage = 0;
private boolean minorMovementDamage = false;
private boolean moderateMovementDamage = false;
private boolean heavyMovementDamage = false;
private boolean infernoFire = false;
private ArrayList<Mounted> jammedWeapons = new ArrayList<Mounted>();
protected boolean engineHit = false;
// locations  
public static final int LOC_BODY = 0;
public static final int LOC_FRONT = 1;
public static final int LOC_RIGHT = 2;
public static final int LOC_LEFT = 3;
public static final int LOC_REAR = 4;
public static final int LOC_TURRET = 5;
// critical hits  
public static final int CRIT_NONE = -1;
public static final int CRIT_DRIVER = 0;
public static final int CRIT_WEAPON_JAM = 1;
public static final int CRIT_WEAPON_DESTROYED = 2;
public static final int CRIT_STABILIZER = 3;
public static final int CRIT_SENSOR = 4;
public static final int CRIT_COMMANDER = 5;
public static final int CRIT_CREW_KILLED = 6;
public static final int CRIT_CREW_STUNNED = 7;
public static final int CRIT_CARGO = 8;
public static final int CRIT_ENGINE = 9;
public static final int CRIT_FUEL_TANK = 10;
public static final int CRIT_AMMO = 11;
public static final int CRIT_TURRET_JAM = 12;
public static final int CRIT_TURRET_LOCK = 13;
public static final int CRIT_TURRET_DESTROYED = 14;
// tanks have no critical slot limitations  
private static final int[] NUM_OF_SLOTS = { 25, 25, 25, 25, 25, 25 };
private static String[] LOCATION_ABBRS = { "BD", "FR", "RS", "LS", "RR", "TU" };
private static String[] LOCATION_NAMES = { "Body", "Front", "Right", "Left", "Rear", "Turret" };
private int sensorHits = 0;
private int stabiliserHits = 0;
private boolean driverHit = false;
private boolean commanderHit = false;
// pain-shunted units can take two driver and commander hits and two hits  
// before being killed  
private boolean driverHitPS = false;
private boolean commanderHitPS = false;
private boolean crewHitPS = false;
}
