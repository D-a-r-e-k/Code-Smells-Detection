/** Find all the light sources in the scene. */
void findLights() {
    Vector<ObjectInfo> lt = new Vector<ObjectInfo>();
    int i;
    positionNeeded = false;
    for (i = 0; i < theScene.getNumObjects(); i++) {
        ObjectInfo info = theScene.getObject(i);
        if (info.getObject() instanceof Light && info.isVisible())
            lt.addElement(info);
    }
    light = new ObjectInfo[lt.size()];
    for (i = 0; i < light.length; i++) {
        light[i] = lt.elementAt(i);
        if (!(light[i].getObject() instanceof DirectionalLight))
            positionNeeded = true;
    }
}
