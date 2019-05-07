void method0() { 
private static final Logger logger = Logger.getLogger(ColonyPlan.class.getName());
// Require production plans to always produce an amount exceeding this. 
private static final int LOW_PRODUCTION_THRESHOLD = 1;
// Number of turns to require production of without exhausting the 
// input goods. 
private static final int PRODUCTION_TURNOVER_TURNS = 5;
private ProfileType profileType;
// Private copy of the AIMain. 
private AIMain aiMain;
// The colony this AIColony manages. 
private Colony colony;
private final List<BuildPlan> buildPlans = new ArrayList<BuildPlan>();
// Comparator to sort buildable types on their priority in the 
// buildPlan map. 
private static final Comparator<BuildPlan> buildPlanComparator = new Comparator<BuildPlan>() {

    public int compare(BuildPlan b1, BuildPlan b2) {
        double d = b1.getValue() - b2.getValue();
        return (d > 0.0) ? -1 : (d < 0.0) ? 1 : 0;
    }
};
// Plans for work locations available to this colony. 
private final List<WorkLocationPlan> workPlans = new ArrayList<WorkLocationPlan>();
// The goods types to produce. 
private final List<GoodsType> produce = new ArrayList<GoodsType>();
// Lists of goods types to be produced in this colony. 
// Temporary variables that do not need to be serialized. 
private final List<GoodsType> foodGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> libertyGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> immigrationGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> militaryGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> rawBuildingGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> buildingGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> rawLuxuryGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> luxuryGoodsTypes = new ArrayList<GoodsType>();
private final List<GoodsType> otherRawGoodsTypes = new ArrayList<GoodsType>();
// Relative weights of the various building categories. 
// TODO: split out/parameterize into a `building strategy' 
// 
// BuildableTypes that improve breeding. 
private static final double BREEDING_WEIGHT = 0.1;
// BuildableTypes that improve building production. 
private static final double BUILDING_WEIGHT = 0.9;
// BuildableTypes that produce defensive units. 
private static final double DEFENCE_WEIGHT = 0.1;
// BuildableTypes that provide export ability. 
private static final double EXPORT_WEIGHT = 0.6;
// BuildableTypes that allow water to be used. 
private static final double FISH_WEIGHT = 0.25;
// BuildableTypes that improve the colony fortifications. 
private static final double FORTIFY_WEIGHT = 0.3;
// BuildableTypes that improve immigration production. 
private static final double IMMIGRATION_WEIGHT = 0.05;
// BuildableTypes that improve liberty production. 
private static final double LIBERTY_WEIGHT = 0.75;
// BuildableTypes that improve military goods production. 
private static final double MILITARY_WEIGHT = 0.4;
// BuildableTypes that improve luxury goods production. 
private static final double PRODUCTION_WEIGHT = 0.25;
// BuildableTypes that improve colony storage. 
private static final double REPAIR_WEIGHT = 0.1;
// BuildableTypes that improve colony storage. 
private static final double STORAGE_WEIGHT = 0.85;
// BuildableTypes that improve education. 
private static final double TEACH_WEIGHT = 0.2;
// BuildableTypes that improve transport. 
private static final double TRANSPORT_WEIGHT = 0.15;
}
