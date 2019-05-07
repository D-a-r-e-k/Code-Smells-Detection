package fitlibraryGeneric.eg.rentEz;

public class Rental {
	private RentalItemType rentalItemType;
	private int count;
	private DateRange dateRange;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public DateRange getDateRange() {
		return dateRange;
	}
	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}
	public RentalItemType getRentalItemType() {
		return rentalItemType;
	}
	public void setRentalItemType(RentalItemType item) {
		this.rentalItemType = item;
	}
}
