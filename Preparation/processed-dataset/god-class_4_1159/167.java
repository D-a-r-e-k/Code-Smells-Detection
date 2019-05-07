/**
     * Gets the price for a recruit in europe.
     *
     * @return The price of a single recruit in {@link Europe}.
     */
public int getRecruitPrice() {
    // return Math.max(0, (getCrossesRequired() - crosses) * 10); 
    return getEurope().getRecruitPrice();
}
