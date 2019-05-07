//}}}  
//{{{ move() method  
public void move(String newPosition) {
    boolean horz = (mode != BROWSER || DockableWindowManager.TOP.equals(newPosition) || DockableWindowManager.BOTTOM.equals(newPosition));
    if (horz == horizontalLayout)
        return;
    horizontalLayout = horz;
    topBox.remove(toolbarBox);
    toolbarBox = new Box(horizontalLayout ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);
    topBox.add(toolbarBox, 0);
    propertiesChanged();
}
