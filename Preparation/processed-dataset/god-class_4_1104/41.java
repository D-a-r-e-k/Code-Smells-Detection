public void writeToFile(DataOutputStream out, Scene theScene) throws IOException {
    super.writeToFile(out, theScene);
    out.writeShort(1);
    out.writeInt(usize);
    out.writeInt(vsize);
    for (int i = 0; i < vertex.length; i++) {
        vertex[i].r.writeToFile(out);
        out.writeInt(vertex[i].ikJoint);
        out.writeDouble(vertex[i].ikWeight);
    }
    for (int i = 0; i < usize; i++) out.writeFloat(usmoothness[i]);
    for (int i = 0; i < vsize; i++) out.writeFloat(vsmoothness[i]);
    out.writeBoolean(uclosed);
    out.writeBoolean(vclosed);
    out.writeInt(smoothingMethod);
    skeleton.writeToStream(out);
}
