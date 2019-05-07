private void setLocationHints(XSDDescription desc, String[] locations, StringList docLocations) {
    int length = locations.length;
    String[] hints = new String[length];
    int counter = 0;
    for (int i = 0; i < length; i++) {
        try {
            String id = XMLEntityManager.expandSystemId(locations[i], desc.getBaseSystemId(), false);
            if (!docLocations.contains(id)) {
                hints[counter++] = locations[i];
            }
        } catch (MalformedURIException e) {
        }
    }
    if (counter > 0) {
        if (counter == length) {
            fXSDDescription.fLocationHints = hints;
        } else {
            fXSDDescription.fLocationHints = new String[counter];
            System.arraycopy(hints, 0, fXSDDescription.fLocationHints, 0, counter);
        }
    }
}
