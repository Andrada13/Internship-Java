package interfaces;


import java.util.List;

import model.Category;
import model.Product;
import model.User;

public interface DatabaseServiceInterface {


	/**
	 * <p>Selects products from the database from a given category.
	 * @param categoryIn
	 * @return
	 * @throws Exception
	 */
	public List<Product> getProductByCategory(String categoryIn) throws Exception;
	/**
	 * <p>Selects all products existing in database.
	 * @return
	 * @throws Exception
	 */
	public List<Product> getAllProducts() throws Exception;
	/**
	 * <p>Select information from database about specific product.
	 * @param nameIn
	 * @return
	 * @throws Exception
	 */
	public Product getProductsByName(String nameIn) throws Exception;
	/**
	 * <p>Select an user from database by name.
	 * @param nameUser
	 * @return
	 * @throws Exception
	 */
	public User getUserByName(String nameUser) throws Exception;
	/**
	 * <p>Select categories from database.
	 * @return
	 * @throws Exception
	 */
	public List<String> getCategoriesName()throws Exception;
	/**
	 * Updates quantity of a product.
	 * @param quantityInp
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public long decreaseQuantity(int quantityInp, String name) throws Exception;
	/**
	 * Updates balance of an user.
	 * @param quantityInp
	 * @param nameUser
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public long decreaseBalance(int quantityInp, String nameUser, String name) throws Exception;
	/**
	 * <p>Updates the quantity of a product.
	 * <p>Updates the balance of an user.
	 * @param prod
	 * @param quantityIn
	 * @param username
	 * @throws Exception
	 */
	public void updateBuy(String prod, int quantityIn, String username) throws Exception;
	/**
	 * <p>Replenish the stock of given product with given amount.
	 * @param prod
	 * @param quantity
	 * @return
	 * @throws Exception
	 */
	public long replenishQuantity(String prod, long quantity) throws Exception;
	/**
	 * <p>Add a new category of products.
	 * @param category
	 * @throws Exception
	 */
	public void addNewCategory(String category) throws Exception;
	/**
	 * <p>Remove specific product from the stock if its quantity is empty.
	 * @param name
	 * @throws Exception
	 */
	public void remove(String name) throws Exception;
	/**
	 * <p>Add new product to the stock.
	 * @param categoryid
	 * @param prodName
	 * @param categoryIn
	 * @param quantityIn
	 * @param priceIn
	 * @throws Exception
	 */
	public void addProducts(Category categoryid, String prodName, String categoryIn, String quantityIn, String priceIn)
			throws Exception ;

	
}
