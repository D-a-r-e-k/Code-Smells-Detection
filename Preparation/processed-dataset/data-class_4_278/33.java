/** Sort the list of MaterialIntersection objects by position along a shadow ray. 
      This is done with a simple insertion sort.  Because the list tends to be very
      short, and is in close to the correct order to begin with, this will generally
      be very fast. */
protected void sortMaterialList(MaterialIntersection matChange[], int count) {
    for (int i = 1; i < count; i++) for (int j = i; j > 0 && matChange[j].dist < matChange[j - 1].dist; j--) {
        MaterialIntersection temp = matChange[j - 1];
        matChange[j - 1] = matChange[j];
        matChange[j] = temp;
    }
}
