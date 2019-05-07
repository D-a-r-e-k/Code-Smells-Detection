/**
     * @return the percentage of the cross reference table that has been read
     */
public double dumpPerc() {
    int total = 0;
    for (int k = 0; k < xrefObj.size(); ++k) {
        if (xrefObj.get(k) != null)
            ++total;
    }
    return total * 100.0 / xrefObj.size();
}
