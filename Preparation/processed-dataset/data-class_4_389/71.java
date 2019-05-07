/**
     * @return suspension factor of vehicle
     */
public int getSuspensionFactor() {
    switch(movementMode) {
        case HOVER:
            if (weight <= 10) {
                return 40;
            }
            if (weight <= 20) {
                return 85;
            }
            if (weight <= 30) {
                return 130;
            }
            if (weight <= 40) {
                return 175;
            }
            return 235;
        case HYDROFOIL:
            if (weight <= 10) {
                return 60;
            }
            if (weight <= 20) {
                return 105;
            }
            if (weight <= 30) {
                return 150;
            }
            if (weight <= 40) {
                return 195;
            }
            if (weight <= 50) {
                return 255;
            }
            if (weight <= 60) {
                return 300;
            }
            if (weight <= 70) {
                return 345;
            }
            if (weight <= 80) {
                return 390;
            }
            if (weight <= 90) {
                return 435;
            }
            return 480;
        case NAVAL:
        case SUBMARINE:
            return 30;
        case TRACKED:
            return 0;
        case WHEELED:
            return 20;
        case VTOL:
            if (weight <= 10) {
                return 50;
            }
            if (weight <= 20) {
                return 95;
            }
            return 140;
        case WIGE:
            if (weight <= 15) {
                return 45;
            }
            if (weight <= 30) {
                return 80;
            }
            if (weight <= 45) {
                return 115;
            }
            if (weight <= 80) {
                return 140;
            }
    }
    return 0;
}
