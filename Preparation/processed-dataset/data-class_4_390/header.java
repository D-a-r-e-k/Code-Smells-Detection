void method0() { 
/**
     *
     */
private static final long serialVersionUID = 4594311535026187825L;
/*
     * Infantry have no critical slot limitations. IS squads usually have 4 men,
     * Clan points usually have 5. Have a location that represents the entire
     * squad.
     */
private static final int[] IS_NUM_OF_SLOTS = { 7, 2, 2, 2, 2, 2, 2 };
private static final String[] IS_LOCATION_ABBRS = { "Squad", "Trooper 1", "Trooper 2", "Trooper 3", "Trooper 4", "Trooper 5", "Trooper 6" };
private static final String[] IS_LOCATION_NAMES = { "Squad", "Trooper 1", "Trooper 2", "Trooper 3", "Trooper 4", "Trooper 5", "Trooper 6" };
private static final int[] CLAN_NUM_OF_SLOTS = { 10, 2, 2, 2, 2, 2, 2 };
private static final String[] CLAN_LOCATION_ABBRS = { "Point", "Trooper 1", "Trooper 2", "Trooper 3", "Trooper 4", "Trooper 5", "Trooper 6" };
private static final String[] CLAN_LOCATION_NAMES = { "Point", "Trooper 1", "Trooper 2", "Trooper 3", "Trooper 4", "Trooper 5", "Trooper 6" };
// these only used by custom ba dialog  
public static final int MANIPULATOR_NONE = 0;
public static final int MANIPULATOR_ARMORED_GLOVE = 1;
public static final int MANIPULATOR_BASIC = 2;
public static final int MANIPULATOR_BASIC_MINE_CLEARANCE = 3;
public static final int MANIPULATOR_BATTLE = 4;
public static final int MANIPULATOR_BATTLE_MAGNET = 5;
public static final int MANIPULATOR_BATTLE_VIBRO = 6;
public static final int MANIPULATOR_HEAVY_BATTLE = 7;
public static final int MANIPULATOR_HEAVY_BATTLE_VIBRO = 8;
public static final int MANIPULATOR_SALVAGE_ARM = 9;
public static final int MANIPULATOR_CARGO_LIFTER = 10;
public static final int MANIPULATOR_INDUSTRIAL_DRILL = 11;
// these only used by custom ba dialog  
public static final String[] MANIPULATOR_TYPE_STRINGS = { "None", "Armored Glove", "Basic Manipulator", "Basic Manipulator (Mine Clearance)", "Battle Claw", "Battle Claw (Magnets)", "Battle Claw (Vibro-Claws)", "Heavy Battle Claw", "Heavy Battle Claw (Vibro-Claws)", "Salvage Arm", "Cargo Lifter", "Industrial Drill" };
public static final int CHASSIS_TYPE_BIPED = 0;
public static final int CHASSIS_TYPE_QUAD = 1;
/**
     * The number of men alive in this unit at the beginning of the phase,
     * before it begins to take damage.
     */
private int troopersShooting = 0;
/**
     * the number of troopers of this squad, dead or alive
     */
private int troopers = -1;
/**
     * The weight of a single trooper
     */
private float trooperWeight = -1.0f;
/**
     * The cost of this unit. This value should be set when the unit's
     * file is read.
     */
private int myCost = -1;
/**
     * This unit's weight class
     */
private int weightClass = -1;
/**
     * this unit's chassis type, should be BattleArmor.CHASSIS_TYPE_BIPED or
     * BattleArmor.CHASSIS_TYPE_QUAD
     */
private int chassisType = -1;
/**
     * Flag that is <code>true</code> when this object's constructor has
     * completed.
     */
private boolean isInitialized = false;
/**
     * Flag that is <code>true</code> when this unit is equipped with stealth.
     */
private boolean isStealthy = false;
/**
     * Flag that is <code>true</code> when this unit is equipped with mimetic
     * Camo.
     */
private boolean isMimetic = false;
/**
     * Flag that is <code>true</code> when this unit is equipped with simple
     * Camo.
     */
private boolean isSimpleCamo = false;
/**
     * Modifiers to <code>ToHitData</code> for stealth.
     */
private int shortStealthMod = 0;
private int mediumStealthMod = 0;
private int longStealthMod = 0;
private String stealthName = null;
// Public and Protected constants, constructors, and methods.  
/**
     * Internal name of the Inner disposable SRM2 ammo pack.
     */
public static final String DISPOSABLE_SRM2_AMMO = "BA-SRM2 (one shot) Ammo";
/**
     * Internal name of the disposable NARC ammo pack.
     */
public static final String DISPOSABLE_NARC_AMMO = "BA-Compact Narc Ammo";
/**
     * The internal name for the Mine Launcher weapon.
     */
public static final String MINE_LAUNCHER = "BAMineLauncher";
/**
     * The internal name for Stealth equipment.
     */
public static final String STEALTH = "Basic Stealth";
/**
     * The internal name for Advanced Stealth equipment.
     */
public static final String ADVANCED_STEALTH = "Standard Stealth";
/**
     * The internal name for Expert Stealth equipment.
     */
public static final String EXPERT_STEALTH = "Improved Stealth";
/**
     * The internal name for Mimetic Camo equipment.
     */
public static final String MIMETIC_CAMO = "Mimetic Armor";
/**
     * The internal name for Simple Camo equipment.
     */
public static final String SIMPLE_CAMO = "Camo System";
/**
     * The internal name for Single-Hex ECM equipment.
     */
public static final String SINGLE_HEX_ECM = "Single-Hex ECM";
/**

    /**
     * The maximum number of men in a battle armor squad.
     */
public static final int BA_MAX_MEN = 6;
/**
     * The location for infantry equipment.
     */
public static final int LOC_SQUAD = 0;
public static final int LOC_TROOPER_1 = 1;
public static final int LOC_TROOPER_2 = 2;
public static final int LOC_TROOPER_3 = 3;
public static final int LOC_TROOPER_4 = 4;
public static final int LOC_TROOPER_5 = 5;
public static final int LOC_TROOPER_6 = 6;
/**
     * have all of this units attacks during a swarm attack already been resolved?
     */
private boolean attacksDuringSwarmResolved = false;
}
