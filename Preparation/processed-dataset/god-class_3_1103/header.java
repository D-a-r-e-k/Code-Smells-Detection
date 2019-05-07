void method0() { 
private double thickness[];
private int endsStyle;
private RenderingMesh cachedMesh;
public static final int OPEN_ENDS = 0;
public static final int CLOSED_ENDS = 1;
public static final int FLAT_ENDS = 2;
private static final int MAX_SUBDIVISIONS = 20;
private static final Property PROPERTIES[] = new Property[] { new Property(Translate.text("menu.smoothingMethod"), new Object[] { Translate.text("menu.none"), Translate.text("menu.interpolating"), Translate.text("menu.approximating") }, Translate.text("menu.shading")), new Property(Translate.text("menu.endsStyle"), new Object[] { Translate.text("menu.openEnds"), Translate.text("menu.closedEnds"), Translate.text("menu.flatEnds") }, Translate.text("menu.openEnds")) };
}
