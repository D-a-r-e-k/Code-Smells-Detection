/**
		 * This method returns a String that represent the object.
		 *
		 * @return a string representing the object.
		 */
@Override
public String toString() {
    return "Login " + (correct ? "successful" : "FAILED") + " at " + date + " from " + address;
}
