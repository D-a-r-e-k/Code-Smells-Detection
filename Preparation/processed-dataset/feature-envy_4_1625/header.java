void method0() { 
/**
     *
     */
private static final long serialVersionUID = -1929593228891136561L;
public static final int NUM_MECH_LOCATIONS = 8;
// system designators for critical hits  
public static final int SYSTEM_LIFE_SUPPORT = 0;
public static final int SYSTEM_SENSORS = 1;
public static final int SYSTEM_COCKPIT = 2;
public static final int SYSTEM_ENGINE = 3;
public static final int SYSTEM_GYRO = 4;
// actuators are systems too, for now  
public static final int ACTUATOR_SHOULDER = 7;
public static final int ACTUATOR_UPPER_ARM = 8;
public static final int ACTUATOR_LOWER_ARM = 9;
public static final int ACTUATOR_HAND = 10;
public static final int ACTUATOR_HIP = 11;
public static final int ACTUATOR_UPPER_LEG = 12;
public static final int ACTUATOR_LOWER_LEG = 13;
public static final int ACTUATOR_FOOT = 14;
public static final String systemNames[] = { "Life Support", "Sensors", "Cockpit", "Engine", "Gyro", null, null, "Shoulder", "Upper Arm", "Lower Arm", "Hand", "Hip", "Upper Leg", "Lower Leg", "Foot" };
// locations  
public static final int LOC_HEAD = 0;
public static final int LOC_CT = 1;
public static final int LOC_RT = 2;
public static final int LOC_LT = 3;
public static final int LOC_RARM = 4;
public static final int LOC_LARM = 5;
public static final int LOC_RLEG = 6;
public static final int LOC_LLEG = 7;
// cockpit status  
public static final int COCKPIT_OFF = 0;
public static final int COCKPIT_ON = 1;
public static final int COCKPIT_AIMED_SHOT = 2;
// gyro types  
public static final int GYRO_UNKNOWN = -1;
public static final int GYRO_STANDARD = 0;
public static final int GYRO_XL = 1;
public static final int GYRO_COMPACT = 2;
public static final int GYRO_HEAVY_DUTY = 3;
public static final String[] GYRO_STRING = { "Standard Gyro", "XL Gyro", "Compact Gyro", "Heavy Duty Gyro" };
public static final String[] GYRO_SHORT_STRING = { "Standard", "XL", "Compact", "Heavy Duty" };
// cockpit types  
public static final int COCKPIT_UNKNOWN = -1;
public static final int COCKPIT_STANDARD = 0;
public static final int COCKPIT_INDUSTRIAL = 1;
public static final int COCKPIT_PRIMITIVE = 2;
public static final int COCKPIT_PRIMITIVE_INDUSTRIAL = 3;
public static final int COCKPIT_SMALL = 4;
public static final int COCKPIT_COMMAND_CONSOLE = 5;
public static final int COCKPIT_TORSO_MOUNTED = 6;
public static final int COCKPIT_DUAL = 7;
public static final String[] COCKPIT_STRING = { "Standard Cockpit", "Industrial Cockpit", "Primitive Cockpit", "Primitive Industrial Cockpit", "Small Cockpit", "Command Console", "Torso-Mounted Cockpit", "Dual Cockpit" };
public static final String[] COCKPIT_SHORT_STRING = { "Standard", "Industrial", "Primitive", "Primitive Industrial", "Small", "Command Console", "Torso Mounted", "Dual" };
public static final String FULL_HEAD_EJECT_STRING = "Full Head Ejection System";
// jump types  
public static final int JUMP_UNKNOWN = -1;
public static final int JUMP_NONE = 0;
public static final int JUMP_STANDARD = 1;
public static final int JUMP_IMPROVED = 2;
public static final int JUMP_BOOSTER = 3;
public static final int JUMP_DISPOSABLE = 4;
// Some "has" items only need be determined once  
public static final int HAS_FALSE = -1;
public static final int HAS_UNKNOWN = 0;
public static final int HAS_TRUE = 1;
// rear armor  
private int[] rearArmor;
private int[] orig_rearArmor;
private static int[] MASC_FAILURE = { 2, 4, 6, 10, 12, 12, 12 };
// MASCLevel is the # of turns MASC has been used previously  
private int nMASCLevel = 0;
private boolean bMASCWentUp = false;
private boolean usedMASC = false;
// Has masc been used?  
private int sinksOn = -1;
private int sinksOnNextRound = -1;
private boolean autoEject = true;
private int cockpitStatus = COCKPIT_ON;
private int cockpitStatusNextRound = COCKPIT_ON;
private int jumpType = JUMP_UNKNOWN;
private int gyroType = GYRO_STANDARD;
private int cockpitType = COCKPIT_STANDARD;
private int cowlArmor = 3;
private int hasLaserHeatSinks = HAS_UNKNOWN;
// For grapple attacks  
private int grappled_id = Entity.NONE;
private boolean isGrappleAttacker = false;
private int grappledSide = Entity.GRAPPLE_BOTH;
private boolean shouldDieAtEndOfTurnBecauseOfWater = false;
private boolean justMovedIntoIndustrialKillingWater = false;
private boolean stalled = false;
private boolean stalledThisTurn = false;
private boolean checkForCrit = false;
private int levelsFallen = 0;
private boolean fullHeadEject = false;
}
