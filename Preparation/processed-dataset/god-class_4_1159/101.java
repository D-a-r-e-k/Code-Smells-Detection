/**
     * Returns how many liberty points in total are needed to earn the
     * Founding Father we are trying to recruit. The description of the
     * algorithm was taken from
     * http://t-a-w.blogspot.com/2007/05/colonization-tips.html
     *
     * @return Total number of liberty points the <code>Player</code>
     *         needs to recruit the next founding father.
     * @see #incrementLiberty
     */
public int getTotalFoundingFatherCost() {
    final Specification spec = getSpecification();
    int base = spec.getInteger("model.option.foundingFatherFactor");
    int count = getFatherCount();
    return ((count + 1) * (count + 2) - 1) * base + count;
}
