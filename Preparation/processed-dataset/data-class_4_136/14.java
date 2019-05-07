/******************************************************************************/
/**
 * Updating the position of the given cell, by taking the direction of the
 * current impulse and the length of the temperature of the cell. After this
 * temperature will fall, when the last impulse and the current impulse are
 * part of a rotation or a oscillation, temperature of the cell will be 
 * decreased.
 * 
 * @param view Cell that should be updated
 */
private void updatePosAndTemp(CellView view) {
    Point2D.Double impulse = (Point2D.Double) view.getAttributes().get(KEY_CURRENT_IMPULSE);
    Point2D.Double lastImpulse = (Point2D.Double) view.getAttributes().get(KEY_LAST_IMPULSE);
    Point2D.Double position = getPosition(view);
    double localTemperature = ((Double) view.getAttributes().get(KEY_TEMPERATURE)).doubleValue();
    double skewGauge = ((Double) view.getAttributes().get(KEY_SKEWGAUGE)).doubleValue();
    double absImpulse = MathExtensions.abs(impulse);
    double absLastImpulse = MathExtensions.abs(lastImpulse);
    if (absImpulse > equalsNull) {
        //if impulse != 0 
        //scaling with temperature 
        if (isCluster(view)) {
            impulse.setLocation(impulse.getX() * localTemperature * clusterForceScalingFactor / absImpulse, impulse.getY() * localTemperature * clusterForceScalingFactor / absImpulse);
        } else {
            impulse.setLocation(impulse.getX() * localTemperature / absImpulse, impulse.getY() * localTemperature / absImpulse);
        }
        view.getAttributes().put(KEY_CURRENT_IMPULSE, impulse);
        position.setLocation(position.getX() + impulse.getX(), position.getY() + impulse.getY());
        view.getAttributes().put(KEY_POSITION, position);
    }
    if (absLastImpulse > equalsNull) {
        //beta = angle between new and last impulse 
        double beta = MathExtensions.angleBetween(impulse, lastImpulse);
        double sinBeta = Math.sin(beta);
        double cosBeta = Math.cos(beta);
        //detection for rotations 
        if (Math.abs(sinBeta) >= Math.sin((Math.PI / 2.0) + (alphaRot / 2.0)))
            skewGauge += sigmaRot * MathExtensions.sgn(sinBeta);
        //detection for oscillation 
        if (cosBeta < Math.cos(Math.PI + (alphaOsc / 2.0)))
            localTemperature *= sigmaOsc * Math.abs(cosBeta);
        localTemperature *= 1.0 - Math.abs(skewGauge);
        localTemperature = Math.min(localTemperature, maxTemperature);
    }
    //applying changes 
    view.getAttributes().put(KEY_TEMPERATURE, new Double(localTemperature));
    view.getAttributes().put(KEY_POSITION, position);
    view.getAttributes().put(KEY_SKEWGAUGE, new Double(skewGauge));
    view.getAttributes().put(KEY_LAST_IMPULSE, new Point2D.Double(impulse.getX(), impulse.getY()));
}
