public int compareTo(BenchmarkResult benchmarkResult) {
    int cmp = this.type - benchmarkResult.type;
    if (cmp == 0) {
        long delta = this.time - benchmarkResult.time;
        cmp = delta > 0 ? 1 : (delta < 0 ? -1 : 0);
    }
    return cmp;
}
