import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Command {

	// private static ArrayList<Product> productsArr = new ArrayList();
	// private static ArrayList<User> usersArr = new ArrayList();
	private static HashSet<String> arrCategories = new HashSet<>();
	private String[] command;
	private static Printer printer = new Printer();
	private static ArrayList<String> messages;

	ConnectDB db = new ConnectDB();

	public String[] getCommand() {
		return command;
	}

	public void setCommand(String[] command) {
		this.command = command;
	}
	/*
	 * public void setProducts(JSONArray products) { for (int i = 0; i <
	 * products.size(); i++) { productsArr.add(new Product((JSONObject)
	 * products.get(i)));
	 * 
	 * } }
	 * 
	 * public void setUsers(JSONArray users) { for (int i = 0; i < users.size();
	 * i++) { usersArr.add(new User((JSONObject) users.get(i)));
	 * 
	 * }
	 * 
	 * }
	 */

	public void readCommand() throws SQLException {
		messages = new ArrayList<>();

		if (command.length == 1 && command[0].equals("HELP")) {
			help();
			return;
		}
		if (command.length == 2) {
			// if (command[0].equals("EXPORT")) {
			// * export(command[1]);
			// * return; }
			if (command[0].equals("PRINT") && command[1].equals("CATEGORIES")) {
				printCategories();
				return;
			}
		}
		if (command.length == 3) {
			if (command[0].equals("PRINT") && command[1].equals("PRODUCTS") && command[2].equals("ALL")) {
				printAll();
				return;
			}
			if (command[0].equals("PRINT") && command[1].equals("PRODUCTS")) {
				productsName(command[2]);
				return;
			}
			if (command[0].equals("REPLENISH")) {
				replenish(command[1], command[2]);
				return;
			}
			if (command[0].equals("REMOVE") && command[1].equals("PRODUCT")) {
				removeProduct(command[2]);
				return;
			}
			// if (command[0].equals("SWITCH") && command[1].equals("DISPLAY_MODE")) {
			// switchDisplayMode(command[2], null);
			//// return;
			// }

		}

		if (command.length == 4) {
			if (command[0].equals("PRINT") && command[1].equals("PRODUCTS") && command[2].equals("CATEGORY")) {
				printCategory(command[3]);
				return;
			}

			if (command[0].equals("ADD") && command[1].equals("NEW") && command[2].equals("CATEGORY")) {
				addCategory(command[3]);
				return;
			}
			// if (command[0].equals("SWITCH") && command[1].equals("DISPLAY_MODE")) {
			// switchDisplayMode(command[2], command[3]);
			// return;
			// }
		}
		if (command.length == 5 && command[0].equals("BUY") && command[3].equals("FOR")) {
			buy(command[1], command[2], command[4]);
			return;
		}

		if (command.length == 7 && (command[0].equals("ADD")) && (command[1].equals("NEW"))
				&& (command[2].equals("PRODUCT"))) {
			addProduct(command[3], command[4], command[5], command[6]);
			return;
		}

		messages.add("That command is not recognizes. Please try typing help for a list of available commands. ");
		printer.print(messages, command);

	}
	/*
	 * // bonus 3 private void export(String newJSON) { messages = new ArrayList();
	 * messages.add(" "); printer.print(messages, command);
	 * 
	 * JSONObject newObj = new JSONObject(); JSONArray arrayPr = new JSONArray();
	 * JSONArray arrayUs = new JSONArray();
	 * 
	 * for (Product product : productsArr) { arrayPr.add(product.getJSONObject()); }
	 * 
	 * for (User user : usersArr) { arrayUs.add(user.getJSONObject()); }
	 * 
	 * newObj.put("stock", arrayPr); newObj.put("clients", arrayUs);
	 * 
	 * try (FileWriter file = new FileWriter(newJSON.replace("\"", ""))) {
	 * file.write(newObj.toJSONString()); file.flush();
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * // bonus 1 private void help() { messages = new ArrayList(); messages.add(
	 * "PRINT PRODUCTS CATEGORY ${CATEGORY_NAME} - Show information about products form a given category"
	 * ); messages.add("PRINT PRODUCTS ALL - Show information about all products");
	 * messages.
	 * add("PRINT PRODUCTS ${PRODUCTS_NAME} - Show information about specific product"
	 * ); messages.add("PRINT CATEGORIES - Show all categories of products");
	 * messages.
	 * add("BUY ${PRODUCT} ${QUANTITY} FOR ${USERNAME} - Buy an amount of a specific product for given user"
	 * ); messages.
	 * add("REPLENISH ${PRODUCT} ${QUANTITY} - Replenish the stock of given product with given amount"
	 * ); messages.add("ADD NEW CATEGORY ${NAME} - Add a new category of products");
	 * messages.
	 * add("ADD NEW PRODUCT ${NAME} ${CATEGORY} ${QUANTITY} ${PRICE} - Add new product to the stock"
	 * ); messages.
	 * add("REMOVE PRODUCT ${NAME} - Remove specific product from the stock if its quantity is empty"
	 * ); messages.add("PRINT DISPLAY_MODE - Print the current display mode");
	 * messages.
	 * add("SWITCH DISPLAY_MODE CONSOLE sau FILE ${CALE_CATRE_FISIER} - Change the display mode"
	 * );
	 * 
	 * printer.print(messages, command);
	 * 
	 * }
	 * 
	 * // comanda 11 private void switchDisplayMode(String newDisplayMode, String
	 * newPath) { messages = new ArrayList(); printer.print(messages, command);
	 * printer.setDisplayMode(newDisplayMode); if (newPath != null) {
	 * printer.setPath(newPath.replace("\"", "")); return; } printer.setPath(null);
	 * }
	 * 
	 * // comanda 10 private void printDisplayMode() { messages = new ArrayList();
	 * messages.add(printer.getDisplayMode()); printer.print(messages, command);
	 * 
	 * }
	 * 
	 * // comanda 9 public void removeProduct(String prodName) { messages = new
	 * ArrayList(); for (Iterator<Product> iterator = productsArr.iterator();
	 * iterator.hasNext();) { Product product = iterator.next(); if (((Product)
	 * product).getName().equals(prodName)) { if (!(((Product)
	 * product).getQuantity() == 0)) { messages.add("Cannot remove " + ((Product)
	 * product).getName() + " " + "because quantity is not zero. Quantity is " +
	 * ((Product) product).getQuantity()); printer.print(messages, command); return;
	 * } else { iterator.remove(); messages.add("Product removed.");
	 * printer.print(messages, command); return; } } }
	 * 
	 * messages.add("No product named ' " + prodName + " ' was found.");
	 * printer.print(messages, command); }
	 * 
	 * // comanda 8 private void addProduct(String prodName, String categoryIn,
	 * String quantityIn, String priceIn) { messages = new ArrayList(); for (Product
	 * product : productsArr) { int quantityInp; int priceInp; try { quantityInp =
	 * Integer.parseInt(quantityIn); priceInp = Integer.parseInt(priceIn);
	 * 
	 * } catch (NumberFormatException nfe) {
	 * messages.add("Error ! The number must be integer."); printer.print(messages,
	 * command); return; }
	 * 
	 * if (!(quantityInp > 0 && priceInp > 0)) {
	 * messages.add("Quantity and price must be positive."); printer.print(messages,
	 * command); return; }
	 * 
	 * if (product.getName().equals(prodName)) {
	 * messages.add("The product already exists."); printer.print(messages,
	 * command); return; } if (!arrCategories.contains(categoryIn)) {
	 * messages.add("The category does not exists."); printer.print(messages,
	 * command); return;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * Product newProduct = new Product(); newProduct.setName(prodName);
	 * newProduct.setCategory(categoryIn);
	 * newProduct.setQuantity(Integer.parseInt(quantityIn));
	 * newProduct.setPrice(Integer.parseInt(priceIn)); productsArr.add(newProduct);
	 * 
	 * messages.add(quantityIn + " " + prodName + " " + "have been added to " +
	 * categoryIn + " " + "category."); printer.print(messages, command);
	 * 
	 * }
	 * 
	 * public void createCategoriesList() { for (Product product : productsArr) {
	 * arrCategories.add((String) product.getCategory()); }
	 * 
	 * }
	 * 
	 * // comanda 7 private void addCategory(String newCategory) { messages = new
	 * ArrayList(); if (arrCategories.add(newCategory)) {
	 * messages.add("Category added."); printer.print(messages, command); } else {
	 * messages.add("Category already exists."); printer.print(messages, command);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * // comanda 6 private void replenish(String prod, String quantityRe) {
	 * messages = new ArrayList(); for (Product product : productsArr) { int
	 * quantityInp; try { quantityInp = Integer.parseInt(quantityRe); } catch
	 * (NumberFormatException nfe) {
	 * messages.add("Error ! The second argument must be integer.");
	 * printer.print(messages, command); return; } if (quantityInp <= 0) {
	 * messages.add("Enter a valid quantity."); printer.print(messages, command);
	 * return; } if (product.getName().equals(prod)) { long newQuantity =
	 * product.getQuantity() + quantityInp; if (product.getQuantity() ==
	 * (product.getMaxQuantity())) { messages.add("The quantity is already full.");
	 * printer.print(messages, command); return;
	 * 
	 * } if (newQuantity > product.getMaxQuantity()) {
	 * messages.add("The given quantity is bigger than the maximum quantity.");
	 * printer.print(messages, command); return; }
	 * 
	 * product.setQuantity(newQuantity);
	 * 
	 * messages.add("Quantity successfully added."); printer.print(messages,
	 * command); return; }
	 * 
	 * }
	 * 
	 * messages.add("No product named ' " + prod + " ' was found.");
	 * printer.print(messages, command);
	 * 
	 * }
	 * 
	 * // comanda 5 private void buy(String prod, String quantityIn, String
	 * username) { messages = new ArrayList(); for (Product product : productsArr) {
	 * if (product.getName().equals(prod)) { for (User user : usersArr) { if
	 * (!(Integer.parseInt(quantityIn) > 0)) {
	 * messages.add("Quantity must be positive."); printer.print(messages, command);
	 * return; } else { if (user.getUsername().equals(username)) { int quantityInp;
	 * try { quantityInp = Integer.parseInt(quantityIn); } catch
	 * (NumberFormatException nfe) {
	 * messages.add("Error ! The number must be integer."); printer.print(messages,
	 * command); return; } long totalPrice = product.getPrice() * quantityInp;
	 * 
	 * if (product.getQuantity() == 0) { messages.add("The product " +
	 * product.getName() + " is no longer in stock."); printer.print(messages,
	 * command); return; } if (!(user.getBalance() > totalPrice)) {
	 * messages.add("Your balance is too low."); printer.print(messages, command);
	 * return; } if (product.getQuantity() < quantityInp) { messages.add("User " +
	 * user.getUsername() + " cannot buy " + quantityIn + " " + product.getName() +
	 * " because there is only " + product.getQuantity() + " " + product.getName() +
	 * " left. "); printer.print(messages, command); return; }
	 * 
	 * long newQuantity = product.getQuantity() - quantityInp; long newBalance =
	 * user.getBalance() - totalPrice;
	 * 
	 * product.setQuantity(newQuantity); user.setBalance(newBalance);
	 * 
	 * messages.add("User " + user.getUsername() + " has bought " + quantityIn + " "
	 * + product.getName()); printer.print(messages, command); return; } } }
	 * messages.add("Client not found."); printer.print(messages, command); return;
	 * } } messages.add("Product not found."); printer.print(messages, command); }
	 * 
	 * // comanda 4 private void printCategories() { messages = new ArrayList();
	 * HashSet<Object> list = new HashSet<>(); for (Product product : productsArr) {
	 * list.add(product.getCategory());
	 * 
	 * } Iterator it = list.iterator(); String s = ""; while (it.hasNext()) { s = s
	 * + it.next(); if (it.hasNext()) { s = s + ","; }
	 * 
	 * }
	 * 
	 * messages.add(s);
	 * 
	 * if (messages.isEmpty()) { messages.add("Categories don't exist. "); }
	 * printer.print(messages, command);
	 * 
	 * }
	 * 
	 * // comanda 3 private void productsName(String nameIn) { messages = new
	 * ArrayList(); for (Product product : productsArr) { if
	 * (product.getName().equals(nameIn)) { messages.add(product.getName() + " " +
	 * product.getQuantity() + " " + product.getPrice()); } }
	 * 
	 * if (messages.isEmpty()) { messages.add("No product named ' " + nameIn +
	 * " ' was found."); } printer.print(messages, command); }
	 * 
	 * // comanda 2 private void printAll() { int j = 1; messages = new ArrayList();
	 * for (Product product : productsArr) { messages.add(j + " " +
	 * product.getName() + " " + product.getQuantity() + " " + product.getCategory()
	 * + " " + product.getPrice()); j++; } printer.print(messages, command); }
	 * 
	 * // comanda 1 private void printCategory(String categoryIn) { int j = 1;
	 * messages = new ArrayList(); for (Product product : productsArr) { if
	 * (product.getCategory().equals(categoryIn)) { messages.add(j + " " +
	 * product.getName() + " " + product.getQuantity() + " " + product.getPrice());
	 * j++; } }
	 * 
	 * if (messages.isEmpty()) { messages.add("No category named ' " + categoryIn +
	 * " ' was found."); } printer.print(messages, command); } }
	 */

	// comanda 1
	/*
	 * private void printCategory(String categoryIn) throws SQLException { int j =
	 * 1; messages = new ArrayList(); String query =
	 * "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID"
	 * ;
	 * 
	 * db.conn = DriverManager.getConnection(url, usernameDB, password);
	 * db.statement = db.conn.createStatement(); db.result =
	 * db.statement.executeQuery(query);
	 * 
	 * while (db.result.next()) { if
	 * (db.result.getString("category").equals(categoryIn)) { messages.add(j + " " +
	 * db.result.getString("name") + " " + db.result.getString("quantity") + " " +
	 * db.result.getString("price"));
	 * 
	 * j++; }
	 * 
	 * }
	 * 
	 * if (messages.isEmpty()) { messages.add("No category named ' " + categoryIn +
	 * " ' was found."); } printer.print(messages, command); }
	 * 
	 * 
	 */

	// comanda 1
	/*
	 * private void printCategory(String categoryIn) throws SQLException { int j =
	 * 1; messages = new ArrayList<>(); // String query = String.format( // "SELECT
	 * *FROM product INNER JOIN categories ON //
	 * product.CategoryID=categories.CategoryID WHERE categories.category='%s'", //
	 * categoryIn);
	 * 
	 * // String query = "SELECT *FROM product INNER JOIN categories ON //
	 * product.CategoryID=categories.CategoryID";
	 * 
	 * db.
	 * createConnection("SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID"
	 * ); // db.setResult(db.getStatement().executeQuery(query)); //
	 * db.executeQuery();
	 * 
	 * while (db.getResult().next()) { if
	 * (db.getResult().getString("category").equals(categoryIn)) { messages.add(j +
	 * " " + db.getResult().getString("namePr") + " " +
	 * db.getResult().getString("quantity") + " " +
	 * db.getResult().getString("price"));
	 * 
	 * j++; } } if (messages.isEmpty()) { messages.add("No category named ' " +
	 * categoryIn + " ' was found."); } printer.print(messages, command); }
	 */

	private void printCategory(String categoryIn) throws SQLException {
		int j = 1;
		messages = new ArrayList<>();

		List<Product> products = db.getProductsByCategory(categoryIn);

		for (Product product : products) {
			if (product.getCategory().equals(categoryIn)) {
				messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + product.getPrice());
				j++;
			}
		}
		if (messages.isEmpty()) {
			messages.add("No category named ' " + categoryIn + " ' was found.");
		}
		printer.print(messages, command);

	}

	// comanda 2
	private void printAll() throws SQLException {
		int j = 1;
		messages = new ArrayList<>();

		List<Product> products = db.getAllProducts();

		for (Product product : products) {
			messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + product.getCategory() + " "
					+ product.getPrice());

			j++;
		}

		printer.print(messages, command);

	}

//comanda 3
	private void productsName(String nameIn) throws SQLException {
		messages = new ArrayList<>();

		Product products = db.getProductsByName(nameIn);

		if (!(products.getName() == null)) {
			messages.add(products.getName() + " " + products.getQuantity() + " " + products.getPrice());
		} else {
			messages.add("No product named ' " + nameIn + " ' was found.");
		}
		printer.print(messages, command);
	}

//comanda 4 
	private void printCategories() throws SQLException {
		messages = new ArrayList<>();
		// HashSet<Object> list = new HashSet<>();

		List<String> list = db.getProductsByCategory();

		Iterator<String> it = list.iterator();
		String s = "";
		while (it.hasNext()) {
			s = s + it.next();
			if (it.hasNext()) {
				s = s + ",";
			}

		}

		messages.add(s);

		// if (messages.isEmpty()) {
		if (list.isEmpty()) {
			messages.add("Categories don't exist. ");
		}
		printer.print(messages, command);

	}

//comanda 5
	private void buy(String prod, String quantityIn, String username) throws SQLException {
		messages = new ArrayList<>();

		Product product = db.getProductsByName(prod);
		User user = db.getUserByName(username);

		if (!(product.getName() == null)) {
			int quantityInp;
			try {
				quantityInp = Integer.parseInt(quantityIn);
			} catch (NumberFormatException nfe) {
				messages.add("Error ! The number must be integer.");
				printer.print(messages, command);
				return;
			}
			if (!(Integer.parseInt(quantityIn) > 0)) {
				messages.add("Quantity must be positive.");
				printer.print(messages, command);
				return;
			} else {
				if (!(user.getUsername() == null)) {
					long totalPrice = product.getPrice() * quantityInp;

					if (product.getQuantity() == 0) {
						messages.add("The product " + product.getName() + " is no longer in stock.");
						printer.print(messages, command);
						return;
					}
					if (!(user.getBalance() > totalPrice)) {
						messages.add("Your balance is too low.");
						printer.print(messages, command);
						return;
					}
					if (product.getQuantity() < quantityInp) {
						messages.add("User " + user.getUsername() + " cannot buy " + quantityIn + " "
								+ product.getName() + " because there is only " + product.getQuantity() + " "
								+ product.getName() + " left. ");
						printer.print(messages, command);
						return;
					}

					db.updateBuy(prod, quantityInp, username);

					messages.add("User " + user.getUsername() + " has bought " + quantityIn + " " + product.getName());
					printer.print(messages, command);
					return;
				}

			}

			messages.add("Client not found.");
			printer.print(messages, command);
			return;

		}

		messages.add("Product not found.");
		printer.print(messages, command);

	}

	// comanda 6
	private void replenish(String prod, String quantityRe) throws SQLException {
		messages = new ArrayList<>();

		int quantityInp;
		try {
			quantityInp = Integer.parseInt(quantityRe);

		} catch (NumberFormatException nfe) {
			messages.add("Error ! The second argument must be integer.");
			printer.print(messages, command);
			return;
		}
		if (quantityInp <= 0) {
			messages.add("Enter a valid quantity.");
			printer.print(messages, command);
			return;
		}

		Product product = db.getProductsByName(prod);

		if (!(product.getName() == null)) {
			if (product.getQuantity() == product.getMaxQuantity()) {
				messages.add("The quantity is already full.");
				printer.print(messages, command);
				return;
			}

			long q = db.replenishQuantity(prod, quantityInp);

			if (q > product.getMaxQuantity()) {
				messages.add("The given quantity is bigger than the maximum quantity.");
				printer.print(messages, command);
				return;
			}
			messages.add("Quantity successfully added.");
			printer.print(messages, command);
			return;
		}

		messages.add("No product named ' " + prod + " ' was found.");
		printer.print(messages, command);
		return;

	}

	public void createCategoriesList() throws SQLException {

		List<String> list = db.getProductsByCategory();
		for (String category : list) {
			arrCategories.add(category);
		}

		System.out.println(arrCategories);
	}

	// comanda 7
	private void addCategory(String newCategory) throws SQLException {
		messages = new ArrayList<>();

		if (arrCategories.add(newCategory)) {

			db.addNewCategory(newCategory);

			messages.add("Category added. ");
			printer.print(messages, command);
			return;
		} else {
			messages.add("Category already exists.");
			printer.print(messages, command);
			return;

		}
	}

	// comanda 8
	private void addProduct(String prod, String categoryIn, String quantityIn, String priceIn) throws SQLException {
		messages = new ArrayList<>();

		int quantityInp;
		int priceInp;
		try {
			quantityInp = Integer.parseInt(quantityIn);
			priceInp = Integer.parseInt(priceIn);

		} catch (NumberFormatException nfe) {
			messages.add("Error ! The number must be integer.");
			printer.print(messages, command);
			return;
		}

		if (!(quantityInp > 0 && priceInp > 0)) {
			messages.add("Quantity and price must be positive.");
			printer.print(messages, command);
			return;
		}
		int categoryid = 0;
		Product product = db.getProductsByName(prod);

		if (product.getName() == null) {
			System.out.println(product.getName() + product.getQuantity());
			if (!arrCategories.contains(categoryIn)) {
				messages.add("The category does not exists.");
				printer.print(messages, command);
				return;
			} else {
				db.addProducts(categoryid, prod, categoryIn, quantityIn, priceIn);
				messages.add(quantityIn + " " + prod + " " + "have been added to " + categoryIn + " " + "category.");
				printer.print(messages, command);
				return;

			}
		}

		messages.add("The product already exists.");
		printer.print(messages, command);
		return;
	}

	// comanda 9
	public void removeProduct(String prodName) throws NumberFormatException, SQLException {
		messages = new ArrayList<>();

		Product product = db.getProductsByName(prodName);

		if (!(product.getName() == null)) {
			if (!(product.getQuantity() == 0)) {
				messages.add("Cannot remove " + product.getName() + " " + "because quantity is not zero. Quantity is "
						+ product.getQuantity());
				printer.print(messages, command);
				return;
			} else {
				db.remove(prodName);
				messages.add("Product removed.");
				printer.print(messages, command);
				return;
			}
		}
		messages.add("No product named ' " + prodName + " ' was found.");
		printer.print(messages, command);

	}

	// bonus 1
	private void help() {
		messages = new ArrayList<>();
		messages.add(
				"PRINT PRODUCTS CATEGORY ${CATEGORY_NAME} - Show information about products form a given category");
		messages.add("PRINT PRODUCTS ALL - Show information about all products");
		messages.add("PRINT PRODUCTS ${PRODUCTS_NAME} - Show information about specific product");
		messages.add("PRINT CATEGORIES - Show all categories of products");
		messages.add("BUY ${PRODUCT} ${QUANTITY} FOR ${USERNAME} - Buy an amount of a specific product for given user");
		messages.add("REPLENISH ${PRODUCT} ${QUANTITY} - Replenish the stock of given product with given amount");
		messages.add("ADD NEW CATEGORY ${NAME} - Add a new category of products");
		messages.add("ADD NEW PRODUCT ${NAME} ${CATEGORY} ${QUANTITY} ${PRICE} - Add new product to the stock");
		messages.add("REMOVE PRODUCT ${NAME} - Remove specific product from the stock if its quantity is empty");
		messages.add("PRINT DISPLAY_MODE - Print the current display mode");
		messages.add("SWITCH DISPLAY_MODE CONSOLE sau FILE ${CALE_CATRE_FISIER} - Change the display mode");

		printer.print(messages, command);

	}

}