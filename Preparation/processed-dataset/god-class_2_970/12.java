/** Updates the progress based on the movements count
	 *
	 */
protected void updateProgress4Movements() {
    // adds the current loop count 
    movements.add(new Integer(movementsCurrentLoop));
    iteration++;
    // if the current loop count is higher than the max movements count 
    // memorize the new max 
    if (movementsCurrentLoop > movementsMax) {
        movementsMax = movementsCurrentLoop;
    }
}
