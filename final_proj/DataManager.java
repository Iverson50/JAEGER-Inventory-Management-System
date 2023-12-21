package final_proj;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<UserManagement.User> userList;
    private List<ProductManagement.Product> productList;

    public DataManager() {
        userList = new ArrayList<>();
        productList = new ArrayList<>();
    }

    public List<UserManagement.User> getUserList() {
        return userList;
    }

    public List<ProductManagement.Product> getProductList() {
        return productList;
    }

    public void loadData() {

        userList.add(new UserManagement.User("user1", "password1"));
        userList.add(new UserManagement.User("user2", "password2"));

        productList.add(new ProductManagement.Product("Product1", "Description1", "Category1", "Location1", 10));
        productList.add(new ProductManagement.Product("Product2", "Description2", "Category2", "Location2", 20));
    }

    public void saveData() {

        System.out.println("User List: " + userList);
        System.out.println("Product List: " + productList);
    }
}
