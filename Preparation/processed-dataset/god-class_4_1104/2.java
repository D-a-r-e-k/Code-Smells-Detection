public void copyObject(Object3D obj) {
    SplineMesh mesh = (SplineMesh) obj;
    texParam = null;
    vertex = new MeshVertex[mesh.vertex.length];
    for (int i = 0; i < mesh.vertex.length; i++) vertex[i] = new MeshVertex(mesh.vertex[i]);
    usmoothness = new float[mesh.usize];
    for (int i = 0; i < mesh.usize; i++) usmoothness[i] = mesh.usmoothness[i];
    vsmoothness = new float[mesh.vsize];
    for (int i = 0; i < mesh.vsize; i++) vsmoothness[i] = mesh.vsmoothness[i];
    setSmoothingMethod(mesh.getSmoothingMethod());
    if (skeleton == null)
        skeleton = mesh.skeleton.duplicate();
    else
        skeleton.copy(mesh.skeleton);
    usize = mesh.usize;
    vsize = mesh.vsize;
    uclosed = mesh.uclosed;
    vclosed = mesh.vclosed;
    copyTextureAndMaterial(obj);
}
