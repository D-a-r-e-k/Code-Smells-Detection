public Rectangle getPrimitive(TaskActivity activity) {
    return (Rectangle) getContainerFor(activity.getTask()).getPrimitive(activity);
}
