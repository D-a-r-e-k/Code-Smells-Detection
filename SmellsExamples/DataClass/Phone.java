public class Phone {
    private String unformattedNumber;

    public Phone(String unformattedNumber) {
        this.unformattedNumber = unformattedNumber;
    }

    public String getAreaCode() {
        return unformattedNumber.substring(0,3);
    }

    public String getPrefix() {
        return unformattedNumber.substring(3,6);
    }

    public String getNumber() {
        return unformattedNumber.substring(6,10);
    }
}
