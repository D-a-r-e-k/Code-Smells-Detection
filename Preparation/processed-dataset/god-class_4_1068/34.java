private void prepareDependencyDrawData(Task task, List result) {
    TaskDependency[] deps = task.getDependencies().toArray();
    for (int i = 0; i < deps.length; i++) {
        TaskDependency next = deps[i];
        TaskDependency.ActivityBinding activityBinding = next.getActivityBinding();
        TaskActivity dependant = activityBinding.getDependantActivity();
        GraphicPrimitiveContainer dependantContainer = getContainerFor(dependant.getTask());
        GraphicPrimitiveContainer.Rectangle dependantRectangle = (Rectangle) dependantContainer.getPrimitive(dependant);
        if (dependantRectangle == null) {
            //System.out.println("dependantRectangle == null"); 
            continue;
        }
        TaskActivity dependee = activityBinding.getDependeeActivity();
        GraphicPrimitiveContainer dependeeContainer = getContainerFor(dependee.getTask());
        GraphicPrimitiveContainer.Rectangle dependeeRectangle = (Rectangle) dependeeContainer.getPrimitive(dependee);
        if (dependeeRectangle == null) {
            //System.out.println("dependeeRectangle == null"); 
            continue;
        }
        Date[] bounds = activityBinding.getAlignedBounds();
        PointVector dependantVector;
        if (bounds[0].equals(dependant.getStart())) {
            dependantVector = new WestPointVector(new Point(dependantRectangle.myLeftX, dependantRectangle.getMiddleY()));
        } else if (bounds[0].equals(dependant.getEnd())) {
            dependantVector = new EastPointVector(new Point(dependantRectangle.getRightX(), dependantRectangle.getMiddleY()));
        } else {
            throw new RuntimeException();
        }
        // 
        PointVector dependeeVector;
        if (bounds[1].equals(dependee.getStart())) {
            dependeeVector = new WestPointVector(new Point(dependeeRectangle.myLeftX, dependeeRectangle.getMiddleY()));
        } else if (bounds[1].equals(dependee.getEnd())) {
            dependeeVector = new EastPointVector(new Point(dependeeRectangle.getRightX(), dependeeRectangle.getMiddleY()));
        } else {
            throw new RuntimeException();
        }
        DependencyDrawData data = new DependencyDrawData(next, dependantRectangle, dependeeRectangle, dependantVector, dependeeVector);
        result.add(data);
    }
}
