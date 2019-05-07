/**
   * Sort the objects in the scene into the most efficient order for rendering.
   */
private ObjectInfo[] sortObjects() {
    class SortRecord implements Comparable {

        public ObjectInfo object;

        public double depth;

        public boolean isTransparent;

        SortRecord(ObjectInfo object) {
            this.object = object;
            depth = theCamera.getObjectToView().times(object.getBounds().getCenter()).z;
            if (object.getObject().getTexture() != null)
                isTransparent = (object.getObject().getTexture().hasComponent(Texture.TRANSPARENT_COLOR_COMPONENT));
        }

        public int compareTo(Object o) {
            SortRecord other = (SortRecord) o;
            if (isTransparent == other.isTransparent) {
                // Order by depth. 
                if (depth < other.depth)
                    return -1;
                if (depth == other.depth)
                    return 0;
                return 1;
            }
            // Put transparent objects last. 
            if (isTransparent)
                return 1;
            return -1;
        }
    }
    ArrayList<SortRecord> objects = new ArrayList<SortRecord>();
    for (int i = 0; i < theScene.getNumObjects(); i++) {
        ObjectInfo obj = theScene.getObject(i);
        theCamera.setObjectTransform(obj.getCoords().fromLocal());
        objects.add(new SortRecord(obj));
    }
    Collections.sort(objects);
    ObjectInfo result[] = new ObjectInfo[objects.size()];
    for (int i = 0; i < result.length; i++) result[i] = objects.get(i).object;
    return result;
}
