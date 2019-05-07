void method0() { 
private List /* <TaskActivity> */
myVisibleActivities;
private static final SortTasksAlgorithm ourAlgorithm = new SortTasksAlgorithm();
private List /* <TaskActivity> */
myCurrentlyProcessed = new ArrayList();
private Map /* <TaskActivity,Integer> */
myActivity2ordinalNumber = new HashMap();
private Map /* <Task, Integer> */
myTask_WorkingRectanglesLength = new HashMap();
private int myPosX;
private TimeFrame myCurrentTimeFrame;
private TimeUnit myCurrentUnit;
private Date myUnitStart;
private boolean myProgressRenderingEnabled = true;
private boolean myDependenciesRenderingEnabled = true;
private GanttLanguage lang = GanttLanguage.getInstance();
private boolean isVisible[] = { false, false, false, false, true, false, true };
private ChartModelImpl myModel;
private GPOption[] myDetailsOptions;
private EnumerationOption[] myLabelOptions;
private GPOptionGroup[] myOptionGroups;
public static final int UP = 0;
public static final int DOWN = 1;
public static final int LEFT = 2;
public static final int RIGHT = 3;
private ArrayList myTasks;
private ArrayList myPreviousStateTasks;
private List myPreviousStateCurrentlyProcessed = new ArrayList();
private static List ourInfoList;
private List myActivitiesOutOfView = new ArrayList();
}
