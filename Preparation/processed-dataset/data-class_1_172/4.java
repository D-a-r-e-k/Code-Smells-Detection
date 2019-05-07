// Factory method to create a phone from the embedded setup table  
public Phone countryRegionNumber(int country, int region, int number) {
    return new Phone(country, region, number);
}
