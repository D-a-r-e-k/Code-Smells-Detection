/******************************************************************************/
public Rectangle getResultBounds() {
    int x = 0;
    int y = 0;
    int w = Integer.parseInt(tf_boundsWidth.getText());
    int h = Integer.parseInt(tf_boundsHeight.getText());
    return new Rectangle(x, y, w, h);
}
