public String toString() {
    StringBuilder b = new StringBuilder();
    b.append(getDescription() + "\n");
    b.append("Iterations: " + getIterations() + "\n");
    b.append("Their total (msec): " + getTheirs() + "\n");
    b.append("Our total (msec): " + getOurs() + "\n");
    b.append("Their average (msec): " + getTheirAvg() + "\n");
    b.append("Our average (msec): " + getOurAvg() + "\n");
    return b.toString();
}
