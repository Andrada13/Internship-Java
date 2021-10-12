package interfaces;


import java.util.List;

import model.Category;
import model.Product;
import model.User;

public interface DatabaseServiceInterface {

	//public Connection createConnection() throws SQLException;
	public List<Product> getProductByCategory(String categoryIn) throws Exception;
	public List<Product> getAllProducts() throws Exception;
	public Product getProductsByName(String nameIn) throws Exception;
	public User getUserByName(String nameUser) throws Exception;
	public List<String> getCategoriesName() throws Exception;
	public long decreaseQuantity(int quantityInp, String name) throws Exception;
	public long decreaseBalance(int quantityInp, String nameUser, String name) throws Exception;
	public void updateBuy(String prod, int quantityIn, String username) throws Exception;
	public long replenishQuantity(String prod, long quantity) throws Exception;
	public void addNewCategory(String category) throws Exception;
	public void remove(String name) throws Exception;
	public void addProducts(Category categoryid, String prodName, String categoryIn, String quantityIn, String priceIn)
			throws Exception ;
	
}
