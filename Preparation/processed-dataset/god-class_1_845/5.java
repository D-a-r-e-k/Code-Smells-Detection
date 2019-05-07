public int compareTo(Object obj) {
    double other = ((Number) obj).doubleValue();
    double diff = value - other;
    // System.out.println(value+" "+precision+" "+diff);  
    if (diff < -precision)
        return -1;
    if (diff > precision)
        return 1;
    if (Double.isNaN(value) && Double.isNaN(other))
        return 0;
    if (Double.isNaN(value))
        return 1;
    if (Double.isNaN(other))
        return -1;
    return 0;
}
