public void setWeightClass(int inWC) {
    switch(inWC) {
        case 0:
            weightClass = EntityWeightClass.WEIGHT_BA_PAL;
            break;
        case 1:
            weightClass = EntityWeightClass.WEIGHT_BA_LIGHT;
            break;
        case 2:
            weightClass = EntityWeightClass.WEIGHT_BA_MEDIUM;
            break;
        case 3:
            weightClass = EntityWeightClass.WEIGHT_BA_HEAVY;
            break;
        case 4:
            weightClass = EntityWeightClass.WEIGHT_BA_ASSAULT;
            break;
    }
}
