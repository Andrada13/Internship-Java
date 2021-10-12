package interfaces;

public interface CommandProcessingServiceInterface {

	public void executeCommand(String[] command) throws Exception;
	public void printCategory(String categoryIn,String[] command) throws Exception;
	public void printAll(String[] command) throws Exception;
    public void productsName(String nameIn,String[] command) throws Exception;
	public void printCategories(String[] command) throws Exception;
    public void buy(String prod, String quantityIn, String username,String[] command) throws Exception;
	public void replenish(String prod, String quantityRe,String[] command) throws Exception;
	public void addCategory(String newCategory,String[] command) throws Exception;
	public void addProduct(String prod, String categoryIn, String quantityIn, String priceIn,String[] command) throws Exception;
	public void removeProduct(String prodName,String[] command) throws NumberFormatException, Exception;
	public void help(String[] command);
	
	
}
