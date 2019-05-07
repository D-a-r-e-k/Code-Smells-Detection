void method0() { 
/**
     * If there are no columns that are totaled, we should not issue a totals row.
     */
private boolean containsTotaledColumns = false;
/**
     * No current reset group.
     */
private static final int NO_RESET_GROUP = 4200;
/**
     * Maps the groups to their current totals.
     */
private Map groupNumberToGroupTotal = new HashMap();
/**
     * The deepest reset group. Resets on an outer group will force any deeper groups to reset as well.
     */
private int deepestResetGroup = NO_RESET_GROUP;
/**
     * Controls when the subgroup is ended.
     */
protected int innermostGroup;
/**
     * Logger.
     */
private Log logger = LogFactory.getLog(MultilevelTotalTableDecorator.class);
/**
     * CSS class applied to grand total totals.
     */
protected String grandTotalSum = "grandtotal-sum";
/**
     * CSS class applied to grand total cells where the column is not totaled.
     */
protected String grandTotalNoSum = "grandtotal-nosum";
/**
     * CSS class applied to grand total lablels.
     */
protected String grandTotalLabel = "grandtotal-label";
/**
     * Grandtotal description.
     */
protected String grandTotalDescription = "Grand Total";
/**
     * CSS class appplied to subtotal headers.
     */
private String subtotalHeaderClass = "subtotal-header";
/**
     * CSS class applied to subtotal labels.
     */
private String subtotalLabelClass = "subtotal-label";
/**
     * Message format for subtotal descriptions.
     */
private MessageFormat subtotalDesc = new MessageFormat("{0} Total");
/**
     * CSS class applied to subtotal totals.
     */
private String subtotalValueClass = "subtotal-sum";
/**
     * Holds the header rows and their content for a particular group.
     */
private List headerRows = new ArrayList(5);
}
