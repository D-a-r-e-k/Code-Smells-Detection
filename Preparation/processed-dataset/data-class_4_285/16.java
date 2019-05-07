int scaleNanos(int nanos) {
    int divisor = nanoScaleFactors[scale];
    return (nanos / divisor) * divisor;
}
