package sirenorder;

public class Order {
    private String coffeeType;
    private String size;
    private boolean syrupAdded;
    private String userId;

    public Order(String coffeeType, String size, boolean syrupAdded, String userId) {
        this.coffeeType = coffeeType;
        this.size = size;
        this.syrupAdded = syrupAdded;
        this.userId = userId;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public String getSize() {
        return size;
    }

    public boolean isSyrupAdded() {
        return syrupAdded;
    }

    public String getUserId() {
        return userId;
    }

    public String toString() {
        return userId + "님의 주문: " + size + " 사이즈 " + coffeeType + (syrupAdded ? " (시럽 추가)" : "");
    }
}
