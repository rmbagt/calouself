package model;

public class Item {
    private String item_id;
    private String seller_id;
    private String item_name;
    private String category;
    private String size;
    private double price;
    private String status;

    public Item(String item_id, String seller_id, String item_name, String category, String size, double price, String status) {
        this.item_id = item_id;
        this.seller_id = seller_id;
        this.item_name = item_name;
        this.category = category;
        this.size = size;
        this.price = price;
        this.status = status;
    }

    // Getters and setters
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }
    public String getSeller_id() { return seller_id; }
    public void setSeller_id(String seller_id) { this.seller_id = seller_id; }
    public String getItem_name() { return item_name; }
    public void setItem_name(String item_name) { this.item_name = item_name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Validation methods
    public static boolean validateItemName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 3;
    }

    public static boolean validateCategory(String category) {
        return category != null && !category.trim().isEmpty() && category.length() >= 3;
    }

    public static boolean validateSize(String size) {
        return size != null && !size.trim().isEmpty();
    }

    public static boolean validatePrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        try {
            double priceValue = Double.parseDouble(price);
            return priceValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

