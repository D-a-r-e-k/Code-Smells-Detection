/** This method splits the photons about their median along a particular axis.  When this returns,
      all the photons before medianPos will have values <= the value in medianPos, and all the ones
      after medianPos will have values >= the value in medianPos. */
private void medianSplit(int start, int end, int medianPos, int axis) {
    float medianEstimate;
    if (start == end)
        return;
    if (end - start == 1) {
        if (axisPosition(start, axis) > axisPosition(end, axis))
            swap(start, end);
        return;
    }
    while (start < end) {
        // Estimate the median value. 
        float a = axisPosition(start, axis);
        float b = axisPosition(start + 1, axis);
        float c = axisPosition(end, axis);
        if (a > b) {
            if (a > c)
                medianEstimate = (b > c ? b : c);
            else
                medianEstimate = a;
        } else {
            if (b > c)
                medianEstimate = (a > c ? a : c);
            else
                medianEstimate = b;
        }
        // Split the photons based on whether they are greater than or less than the median estimate. 
        int i = start, j = end;
        while (true) {
            for (; i < end && axisPosition(i, axis) < medianEstimate; i++) ;
            for (; axisPosition(j, axis) > medianEstimate; j--) ;
            if (i >= j)
                break;
            swap(i, j);
            i++;
            j--;
        }
        swap(i, end);
        if (i > medianPos)
            end = i - 1;
        if (i <= medianPos)
            start = i;
    }
}
