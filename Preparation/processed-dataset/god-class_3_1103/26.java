public void writeToFile(DataOutputStream out, Scene theScene) throws IOException {
    super.writeToFile(out, theScene);
    out.writeShort(1);
    for (int i = 0; i < thickness.length; i++) out.writeDouble(thickness[i]);
    out.writeInt(endsStyle);
}
