public String getBuildableReport() {
    String ret = "Buildables:\n";
    for (BuildPlan b : buildPlans) ret += b.toString() + "\n";
    return ret;
}
