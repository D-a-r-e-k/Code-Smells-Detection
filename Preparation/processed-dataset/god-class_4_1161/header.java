void method0() { 
private static final Logger logger = Logger.getLogger(AIColony.class.getName());
private static final String LIST_ELEMENT = "ListElement";
// Do not perform tile improvements that would leave less than 
// this amount of forested work locations available to the colony. 
private static final int FOREST_MINIMUM = 1;
// Do not bother trying to ship out less than this amount of goods. 
private static final int EXPORT_MINIMUM = 10;
// The colony this AIColony is managing. 
private Colony colony;
// The current production plan for the colony. 
private ColonyPlan colonyPlan;
// Goods to export from the colony. 
private final List<AIGoods> aiGoods;
// Useful things for the colony. 
private final List<Wish> wishes;
// Plans to improve neighbouring tiles. 
private final List<TileImprovementPlan> tileImprovementPlans;
// When should the workers in this Colony be rearranged? 
private Turn rearrangeTurn = new Turn(0);
// Goods that should be completely exported and only exported to 
// prevent the warehouse filling. 
private static final Set<GoodsType> fullExport = new HashSet<GoodsType>();
private static final Set<GoodsType> partExport = new HashSet<GoodsType>();
// Comparator to favour expert scouts, then units in that role, 
// then least skillful. 
private static final Comparator<Unit> scoutComparator = new Comparator<Unit>() {

    public int compare(Unit u1, Unit u2) {
        boolean a1 = u1.hasAbility(Ability.EXPERT_SCOUT);
        boolean a2 = u2.hasAbility(Ability.EXPERT_SCOUT);
        if (a1 != a2)
            return (a1) ? -1 : 1;
        a1 = u1.hasAbility("model.ability.scoutIndianSettlement");
        a2 = u2.hasAbility("model.ability.scoutIndianSettlement");
        if (a1 != a2)
            return (a1) ? -1 : 1;
        return u1.getType().getSkill() - u2.getType().getSkill();
    }
};
}
