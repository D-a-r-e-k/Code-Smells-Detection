void method0() { 
/** the logger instance. */
private static final marauroa.common.Logger logger = Log4J.getLogger(RPObject.class);
/** We are interested in clearing added and deleted only if they have changed. */
private boolean modified;
/** a list of events that this object contains */
private List<RPEvent> events;
/** a list of links that this object contains. */
private List<RPLink> links;
/** a map to store maps with keys and values **/
private Map<String, RPObject> maps;
/** Which object contains this one. */
private SlotOwner container;
/** In which slot are this object contained */
private RPSlot containerSlot;
/** added slots, used at Delta^2 */
private List<String> addedSlots;
/** deleted slots, used at Delta^2 */
private List<String> deletedSlots;
/** added slots, used at Delta^2 */
private List<String> addedLinks;
/** deleted slots, used at Delta^2 */
private List<String> deletedLinks;
/** added maps, used at Delta^2 */
private List<String> addedMaps;
/** deleted maps, used at Delta^2 */
private List<String> deletedMaps;
/** Defines an invalid object id */
public static final ID INVALID_ID = new ID(-1, "");
/**
	 * If this variable is true the object is removed from the perception send
	 * to client.
	 */
private boolean hidden;
/** Defines if this object should be stored at database. */
private boolean storable;
}
