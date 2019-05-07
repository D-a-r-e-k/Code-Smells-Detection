void method0() { 
MeshVertex vertex[];
Skeleton skeleton;
boolean uclosed, vclosed;
BoundingBox bounds;
int usize, vsize, cachedUSize, cachedVSize, smoothingMethod;
float usmoothness[], vsmoothness[];
RenderingMesh cachedMesh;
WireframeMesh cachedWire;
private static final int MAX_SUBDIVISIONS = 20;
private static final Property PROPERTIES[] = new Property[] { new Property(Translate.text("menu.smoothingMethod"), new Object[] { Translate.text("menu.interpolating"), Translate.text("menu.approximating") }, Translate.text("menu.shading")), new Property(Translate.text("menu.closed"), new Object[] { Translate.text("menu.udirection"), Translate.text("menu.vdirection"), Translate.text("menu.both"), Translate.text("menu.neither") }, Translate.text("menu.neither")) };
}
