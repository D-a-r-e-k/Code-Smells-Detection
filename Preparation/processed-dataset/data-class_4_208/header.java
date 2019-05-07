void method0() { 
/** The search command action. */
public static final String ACTION_SEARCH = "Search";
/** The cancel command action. */
public static final String ACTION_CANCEL = "Cancel";
/** The add command action. */
public static final String ACTION_ADD_MEMBERS = "Add";
/** The remove command action. */
public static final String ACTION_REMOVE = "Remove";
/** The add command action. */
public static final String SESSION_SELECTIONS = "GroupMemberSelections";
/** The add command action. */
public static final String PARAM_SELECTIONS = "selections";
/** The search field. */
private String search = null;
/** the page number of the search result field. */
private int pageNumber = 0;
/** the search result page size. */
private String selectedPageSize;
/** the action command field. */
private String actionCmd = null;
/** the selected group member oids field.*/
private long[] memberOids = null;
/** the selected parent name to remove.*/
private int memberIndex = 0;
/** the selected parent name to remove.*/
private int selectedCount = 0;
}
