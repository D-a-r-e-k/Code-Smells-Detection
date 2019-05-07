void checkCff() {
    int table_location[];
    table_location = tables.get("CFF ");
    if (table_location != null) {
        cff = true;
        cffOffset = table_location[0];
        cffLength = table_location[1];
    }
}
