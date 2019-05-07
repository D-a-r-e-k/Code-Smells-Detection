/**
	 * Returns the transformation matrix of the image.
	 *
	 * @return an array [AX, AY, BX, BY, CX, CY, DX, DY]
	 */
public float[] matrix() {
    float[] matrix = new float[8];
    float cosX = (float) Math.cos(rotationRadians);
    float sinX = (float) Math.sin(rotationRadians);
    matrix[AX] = plainWidth * cosX;
    matrix[AY] = plainWidth * sinX;
    matrix[BX] = -plainHeight * sinX;
    matrix[BY] = plainHeight * cosX;
    if (rotationRadians < Math.PI / 2f) {
        matrix[CX] = matrix[BX];
        matrix[CY] = 0;
        matrix[DX] = matrix[AX];
        matrix[DY] = matrix[AY] + matrix[BY];
    } else if (rotationRadians < Math.PI) {
        matrix[CX] = matrix[AX] + matrix[BX];
        matrix[CY] = matrix[BY];
        matrix[DX] = 0;
        matrix[DY] = matrix[AY];
    } else if (rotationRadians < Math.PI * 1.5f) {
        matrix[CX] = matrix[AX];
        matrix[CY] = matrix[AY] + matrix[BY];
        matrix[DX] = matrix[BX];
        matrix[DY] = 0;
    } else {
        matrix[CX] = 0;
        matrix[CY] = matrix[AY];
        matrix[DX] = matrix[AX] + matrix[BX];
        matrix[DY] = matrix[BY];
    }
    return matrix;
}
