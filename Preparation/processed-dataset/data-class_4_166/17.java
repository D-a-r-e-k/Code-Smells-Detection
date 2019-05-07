protected boolean isFillable(int decoration) {
    return !(decoration == GraphConstants.ARROW_SIMPLE || decoration == GraphConstants.ARROW_LINE || decoration == GraphConstants.ARROW_DOUBLELINE);
}
