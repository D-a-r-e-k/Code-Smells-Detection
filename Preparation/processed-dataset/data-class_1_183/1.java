//    public Paint getDrawPaint(V v) 
//    { 
//        return draw_paint; 
//    } 
public Paint transform(V v) {
    if (pi.isPicked(v))
        return picked_paint;
    else
        return fill_paint;
}
