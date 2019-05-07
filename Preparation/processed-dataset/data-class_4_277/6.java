/** Spawn a Photon, and see whether it hits anything in the scene.  If so, add it to the map.
      @param r         the ray along which to spawn the photon
      @param color     the photon color
      @param indirect  specifies whether the photon's source should be treated as indirect illumination
  */
public void spawnPhoton(Ray r, RGBColor color, boolean indirect) {
    if (!r.intersects(bounds))
        return;
    OctreeNode node = rt.rootNode.findNode(r.getOrigin());
    if (node == null)
        node = rt.rootNode.findFirstNode(r);
    if (node == null)
        return;
    color = color.duplicate();
    RTObject materialObject = rt.getMaterialAtPoint(r.rt, r.getOrigin(), node);
    if (materialObject == null)
        tracePhoton(r, color, 0, node, null, null, null, null, null, 0.0, indirect, false);
    else
        tracePhoton(r, color, 0, node, null, materialObject.getMaterialMapping(), null, materialObject.toLocal(), null, 0.0, indirect, false);
}
