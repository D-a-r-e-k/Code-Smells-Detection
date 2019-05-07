// return the index in the List tasks of the previous Task which have the 
// same ID as next 
// return -1 if there is no previous Task with the next ID 
private int getPreviousStateTaskIndex(Task task) {
    if (myPreviousStateTasks == null)
        return -1;
    for (int i = 0; i < myPreviousStateTasks.size(); i++) {
        if (task.getTaskID() == ((GanttPreviousStateTask) myPreviousStateTasks.get(i)).getId())
            return i;
    }
    return -1;
}
