/******************************************************************************/
public Rectangle getLayoutUpdateResultBounds() {
    int x = 0;
    int y = 0;
    int w = Integer.parseInt(tf_lu_boundsWidth.getText());
    int h = Integer.parseInt(tf_lu_boundsHeight.getText());
    return new Rectangle(x, y, w, h);
}
