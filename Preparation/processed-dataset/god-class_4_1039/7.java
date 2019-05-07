/**
	 * Returns the state (STATE_APPROVED, STATE_REJECTED etc). This can be used
	 * instead of instanceof, which is useful if the object isn't fully 
	 * initialised by Hibernate.
	 * @return STATE_APPROVED, STATE_REJECTED etc
	 * @hibernate.property column="AT_STATE"
	 */
public int getState() {
    return state;
}
