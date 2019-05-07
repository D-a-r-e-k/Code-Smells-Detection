// This if for subclassers that to not want the graphics 
// to be relative to the top, left corner of this component. 
// Note: Override this method with an empty implementation 
// if you want absolute positions for your edges 
protected void translateGraphics(Graphics g) {
    g.translate(-getX(), -getY());
}
