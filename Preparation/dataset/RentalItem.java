package fitlibraryGeneric.eg.rentEz;

public class RentalItem {
	private RentalItemType item;
	private int count;
	private Duration duration;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public RentalItemType getItem() {
		return item;
	}
	public void setItem(RentalItemType item) {
		this.item = item;
	}
}
