package interfaces;


import java.util.List;


public interface CommandProcessingServiceInterface {

	/**
	 * <p> Receives an array of strings as parameter and calls other methods based on the user input.
	 * @param command
	 * @throws Exception
	 */
	public void executeCommand(String[] command) throws Exception;
	/**
	 * <p>Show information about products from a given category.
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public List<String> printProductsByCategory(String category) throws Exception;
	/**
	 * <p>Show information about all products existing in database.
	 * @return
	 * @throws Exception
	 */
	public List<String> printProducts() throws Exception;
	/**
	 * <p>Show information about specific product.
	 * @param name
	 * @return
	 * @throws Exception
	 */
    public List<String> printProductsByName(String name) throws Exception;
    /**
     * Prints all categories existing in database.
     * @return
     * @throws Exception
     */
	public List<String> printCategories() throws Exception;
    /**
     * <p>Buy an amount of a specific product for given user.
     * <p>Updates the quantity of a product if a user buys it.
     * <p>Updates the balance of an user depending on the quantity and product bought.
     * @param name
     * @param quantity
     * @param username
     * @return
     * @throws Exception
     */
    public List<String> buy(String name, String quantity, String username) throws Exception;
    /**
     * <p>Prints the details of a product if quantity is updated.
     * @param name
     * @param quantity
     * @return
     * @throws Exception
     */
	public List<String> replenishQuantity(String name, String quantity) throws Exception;
	/**
	 * <p>Prints a messages if a new category is added.
	 * @param newCategory
	 * @return
	 * @throws Exception
	 */
	public List<String> addCategory(String newCategory) throws Exception;
	/**
	 * <p>Prints the details of a new product.
	 * @param name
	 * @param category
	 * @param quantity
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public List<String> addProduct(String name, String category, String quantity, String price) throws Exception;
	/**
	 * <p> Prints messages if a product is removed.
	 * @param name
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public List<String> removeProduct(String name) throws NumberFormatException, Exception;
	/**
	 *<p>Prints a list of all existing commands in application.
	 * @param command
	 * @return 
	 */
	public List<String> help();
	
	
}
