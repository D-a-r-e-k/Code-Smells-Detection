import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Library {

    private Catalog currentCatalog;
    private Item currentItem;
    private List<Item> items;
    private List<Catalog> catalogs;

    private Date lastInventoryDate;
    private Queue<Item> itemsToBeApproved;

    private final int BATCH_SIZE = 10;

    public Library() {
        items = new ArrayList<Item>();
        catalogs = new ArrayList<Catalog>();
        itemsToBeApproved = new LinkedList<Item>();

        lastInventoryDate = new Date();
    }

    public void addNewItem(Item item) {
        itemsToBeApproved.add(item);
    }

    public boolean isItemApplicable(Item item) {
        LocalDate currentDate = LocalDate.now();
        Date d1 = Date.from(currentDate.minusYears(10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return d1.before(item.publishedDate);
    }

    public void doInventory() {

        for (var item : items) {
            item.renew();
        }

        for (var catalog : catalogs) {
            catalog.renew();
            catalog.update();
        }

        sortCatalog();

        int i = 0;
        while (i++ < BATCH_SIZE && !itemsToBeApproved.isEmpty()) {
            var item = itemsToBeApproved.poll();

            if (isItemApplicable(item)) {
                items.add(item);
            }
        }

        lastInventoryDate = new Date();
    }

    public void searchLibrary(String code) {

        currentCatalog = catalogs.stream().filter(c -> code.equals(c.code)).findFirst().orElse(null);
    }

    public void checkoutItem() throws Exception {

        if (currentItem == null) {
            throw new Exception("No item was selected.");
        }

        if  (currentItem.borrowedDate != null) {
            throw new Exception("This item is already taken.");
        }

        currentItem.borrowedDate = new Date();
    }

    public void checkinItem() throws Exception {

        if (currentItem == null) {
            throw new Exception("No item was selected.");
        }

        if  (currentItem.borrowedDate == null) {
            throw new Exception("This item is not taken.");
        }

        currentItem.borrowedDate = null;
    }

    public void printCatalog() {
        if (currentCatalog != null) {

            System.out.println("Catalog info: ");
            System.out.println(currentCatalog.name);
            System.out.println(currentCatalog.code);
        } else {
            System.out.println("No catalog is selected");
        }
    }

    public void sortCatalog() {
        Collections.sort(catalogs, (c1, c2) -> c1.code.compareTo(c2.code));
    }

    public void searchCatalog(String code) {
        currentItem = items.stream().filter(i -> code.equals(i.code)).findFirst().orElse(null);
    }

    public boolean isLibraryHasToBePunished() throws Exception {
        if (lastInventoryDate == null) {
            throw new Exception("Inventory did not happen at creation");
        }

        LocalDate currentDate = LocalDate.now();
        Date d1 = Date.from(currentDate.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return d1.after(lastInventoryDate);
    }

    public void punishLibrary() throws Exception {
        if (isLibraryHasToBePunished()) {
            System.out.println("Library is being punished.");
        }
    }
}
