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
		db.createConnection();

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

	private List<Product> printCategory(String categoryIn) throws SQLException {
		int j = 1;
		messages = new ArrayList<>();

		String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
		db.result = db.statement.executeQuery(query);
		
		List<Product> products = new ArrayList<Product>();
		Product product = new Product();
		Categories category = new Categories();

		while (db.result.next()) {
			category.setCategory(db.result.getString("category"));
			if (category.getCategory().equals(categoryIn)) {
				product.setName(db.result.getString("namePr"));
				product.setQuantity(db.result.getLong("quantity"));
				product.setPrice(db.result.getLong("price"));
				products.add(product);

				messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + product.getPrice());
				j++;

			}
		}
		printer.print(messages, command);
		return products;
	}

	// Product product = new Product();

	// List<ResultSet> arrayList = new ArrayList<>(Arrays.asList(db.getResult()));
	// for(ResultSet val : arrayList){
	// System.out.print(val + " ");
	// }

	// return arrayList;
	// db.executeQuery();

	// while (db.getResult().next()) {
	// if (db.getResult().getString("category").equals(categoryIn)) {
	// messages.add(j + " " + db.getResult().getString("namePr") + " " +
	// db.getResult().getString("quantity")
	// + " " + db.getResult().getString("price"));

	// j++;
	// }
	// }
	// if (messages.isEmpty()) {
	// messages.add("No category named ' " + categoryIn + " ' was found.");

	// comanda 2
	private List<Product> printAll() throws SQLException {
		int j = 1;
		messages = new ArrayList<>();
		String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
//?????
		// db.createConnection();

		db.result = db.statement.executeQuery(query);
		List<Product> list = new ArrayList<Product>();

		while (db.result.next()) {
			Product product = new Product();
			Categories category = new Categories();

			product.setName(db.result.getString("namePr"));
			product.setQuantity(db.result.getLong("quantity"));
			category.setCategory(db.result.getString("category"));
			product.setPrice(db.result.getLong("price"));
			list.add(product);

			messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + category.getCategory() + " "
					+ product.getPrice());
			j++;

		}
		printer.print(messages, command);

		return list;

	}

//comanda 3
	private void productsName(String nameIn) throws SQLException {
		messages = new ArrayList<>();
		// String query = String.format(
		// "SELECT *FROM product INNER JOIN categories ON
		// product.CategoryID=categories.CategoryID WHERE product.name='%s'",
		// nameIn);

		db.createConnection("SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID");
		// db.setResult(db.getStatement().executeQuery(query));

		while (db.getResult().next()) {
			if (db.getResult().getString("namePr").equals(nameIn)) {
				messages.add(db.getResult().getString("namePr") + " " + db.getResult().getString("quantity") + " "
						+ db.getResult().getString("price"));
			}
		}
		if (messages.isEmpty()) {
			messages.add("No product named ' " + nameIn + " ' was found.");
		}
		printer.print(messages, command);
	}

//comanda 4 
	private void printCategories() throws SQLException {
		messages = new ArrayList<>();
		HashSet<Object> list = new HashSet<>();
		// String query = "select *from categories";

		db.createConnection("select *from categories");
		// db.setResult(db.getStatement().executeQuery(query));

		while (db.getResult().next()) {
			list.add(db.getResult().getString("category"));

		}
		Iterator<Object> it = list.iterator();
		String s = "";
		while (it.hasNext()) {
			s = s + it.next();
			if (it.hasNext()) {
				s = s + ",";
			}

		}

		messages.add(s);

		if (messages.isEmpty()) {
			messages.add("Categories don't exist. ");
		}
		printer.print(messages, command);

	}

//comanda 5
	private void buy(String prod, String quantityIn, String username) throws SQLException {
		messages = new ArrayList<>();

		// String queryProduct = "select *from product";
		// String queryClients = "select *from clients";
		String updateQueryProduct = "UPDATE product,clients" + "SET quantity = ? and balance  = ? "
				+ " WHERE name = ? and username  = ?";
		// String updateQueryProduct = "UPDATE product " + "SET quantity = ? " + "WHERE
		// name = ?";
		// String updateQueryClients = "UPDATE clients " + "SET balance = ? "
		// + "WHERE username = ?";

		db.createConnection();
		// db.executeQuery();

		// db.executeQueryProducts();
		// db.executeQueryClients();
		// db.setResult(db.getStatement().executeQuery(queryProduct));
		// db.setResultClients(db.getStatementClients().executeQuery(queryClients));
		PreparedStatement psUpdateProduct = db.getConn().prepareStatement(updateQueryProduct);
		PreparedStatement psUpdateClients = db.getConn().prepareStatement(updateQueryClients);

		// db.getConn().setAutoCommit(false);

		while (db.getResult().next()) {
			if (db.getResult().getString("name").equals(prod)) {
				while (db.getResultClients().next()) {
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
						if (db.getResultClients().getString("username").equals(username)) {

							long totalPrice = Integer.parseInt(db.getResult().getString("price")) * quantityInp;

							if (Integer.parseInt(db.getResult().getString("quantity")) == 0) {
								messages.add(
										"The product " + db.getResult().getString("name") + " is no longer in stock.");
								printer.print(messages, command);
								return;
							}
							if (!(Integer.parseInt(db.getResultClients().getString("balance")) > totalPrice)) {
								messages.add("Your balance is too low.");
								printer.print(messages, command);
								return;
							}
							if (Integer.parseInt(db.getResult().getString("quantity")) < quantityInp) {
								messages.add("User " + db.getResultClients().getString("username") + " cannot buy "
										+ quantityIn + " " + db.getResult().getString("name")
										+ " because there is only " + db.getResult().getString("quantity") + " "
										+ db.getResult().getString("name") + " left. ");
								printer.print(messages, command);
								return;
							}

							long newQuantity = Integer.parseInt(db.getResult().getString("quantity")) - quantityInp;
							long newBalance = Integer.parseInt(db.getResultClients().getString("balance")) - totalPrice;

							psUpdateProduct.setLong(1, newQuantity);
							psUpdateProduct.setString(2, prod);
							psUpdateProduct.executeUpdate();

							psUpdateClients.setLong(1, newBalance);
							psUpdateClients.setString(2, username);
							psUpdateClients.executeUpdate();

							db.getConn().commit();

							// pstmt.executeUpdate();

							// psUpdateProduct.addBatch();
							// psUpdateClients.addBatch();
							// psUpdateProduct.executeBatch();
							// psUpdateClients.executeBatch();
							// db.getConn().commit();

							messages.add("User " + db.getResultClients().getString("username") + " has bought "
									+ quantityIn + " " + db.getResult().getString("name"));
							printer.print(messages, command);
							return;
						}

					}
				}

				messages.add("Client not found.");
				printer.print(messages, command);
				return;

			}
		}
		messages.add("Product not found.");
		printer.print(messages, command);

	}

	// comanda 6
	private void replenish(String prod, String quantityRe) throws SQLException {
		messages = new ArrayList<>();

		String sqlUpdate = "UPDATE product " + "SET quantity = ? " + "WHERE namePr = ?";
		// String queryProduct = "select *from product";

		db.createConnection("select *from product");
		// db.executeQueryProducts();
		// db.setResult(db.getStatement().executeQuery(queryProduct));
		PreparedStatement psUpdate = db.getConn().prepareStatement(sqlUpdate);

		while (db.getResult().next()) {
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
			if (db.getResult().getString("namePr").equals(prod)) {
				long newQuantity = Integer.parseInt(db.getResult().getString("quantity")) + quantityInp;
				if (db.getResult().getString("quantity") == (db.getResult().getString("maxQuantity"))) {
					messages.add("The quantity is already full.");
					printer.print(messages, command);
					return;

				}
				if (newQuantity > Integer.parseInt(db.getResult().getString("maxQuantity"))) {
					messages.add("The given quantity is bigger than the maximum quantity.");
					printer.print(messages, command);
					return;
				}

				psUpdate.setLong(1, newQuantity);
				psUpdate.setString(2, prod);
				psUpdate.executeUpdate();

				messages.add("Quantity successfully added.");
				printer.print(messages, command);
				return;
			}

		}

		messages.add("No product named ' " + prod + " ' was found.");
		printer.print(messages, command);

	}

	public void createCategoriesList() throws SQLException {

		// String queryCategories = "select *from categories";
		db.createConnection("select *from categories");
		// db.setResult(db.getStatement().executeQuery(queryCategories));
		while (db.getResult().next()) {
			arrCategories.add(db.getResult().getString("category"));
		}

		System.out.println(arrCategories);
	}

	// comanda 7
	private void addCategory(String newCategory) throws SQLException {
		messages = new ArrayList<>();
		// String queryCategories = "select *from categories";

		db.createConnection("select *from categories");
		// db.setResult(db.getStatement().executeQuery(queryCategories));

		while (db.getResult().next()) {
			if (arrCategories.add(newCategory)) {
				String queryInsert = "insert into categories (category) values (?)";
				PreparedStatement psInsert = db.getConn().prepareStatement(queryInsert);
				psInsert.setString(1, newCategory);
				psInsert.executeUpdate();
				messages.add("Category added.");
				printer.print(messages, command);
				return;
			} else {
				messages.add("Category already exists.");
				printer.print(messages, command);
				return;

			}

		}
	}

	// comanda 8
	private void addProduct(String prodName, String categoryIn, String quantityIn, String priceIn) throws SQLException {
		messages = new ArrayList<>();
		// String query = "SELECT *FROM product INNER JOIN categories ON
		// product.CategoryID=categories.CategoryID";
		// db.createConnection("SELECT product.namePr FROM product INNER JOIN categories
		// ON product.CategoryID=categories.CategoryID");
		db.createConnection("SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID");
		// db.setResult(db.getStatement().executeQuery(query));

		db.getConn().setAutoCommit(false);

		String queryInsert = "INSERT INTO product (CategoryID,namePr, quantity, price) VALUES (?,?, ?, ?)";
		PreparedStatement psInsert = db.getConn().prepareStatement(queryInsert);

		while (db.getResult().next()) {
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

			if (db.getResult().getString("namePr").equals(prodName)) {
				messages.add("The product already exists.");
				printer.print(messages, command);
				return;
			}
			if (!arrCategories.contains(categoryIn)) {
				messages.add("The category does not exists.");
				printer.print(messages, command);
				return;

			}
			if (db.getResult().getString("category").equals(categoryIn)) {
				psInsert.setInt(1, db.getResult().getInt("CategoryID"));

			}
		}

		psInsert.setString(2, prodName);
		psInsert.setString(3, quantityIn);
		psInsert.setString(4, priceIn);

		psInsert.addBatch();
		psInsert.executeBatch();
		db.getConn().commit();

		messages.add(quantityIn + " " + prodName + " " + "have been added to " + categoryIn + " " + "category.");
		printer.print(messages, command);

	}

	// comanda 9
	public void removeProduct(String prodName) throws NumberFormatException, SQLException {
		messages = new ArrayList<>();

		// String queryProduct = String.format("select *from product where name='%s'",
		// prodName);
		db.createConnection("select *from product");
		// db.setResult(db.getStatement().executeQuery(queryProduct));

		while (db.getResult().next()) {
			if (!((Integer.parseInt(db.getResult().getString("quantity")) == 0))) {
				messages.add("Cannot remove " + db.getResult().getString("namePr") + " "
						+ "because quantity is not zero. Quantity is " + db.getResult().getString("quantity"));
				printer.print(messages, command);
				return;
			} else {
				String queryDelete = "delete from product where name=?";
				PreparedStatement psDelete = db.getConn().prepareStatement(queryDelete);
				psDelete.setString(1, prodName);
				psDelete.executeUpdate();
				messages.add("Product removed.");
				printer.print(messages, command);
				return;
			}

		}

		messages.add("No product named ' " + prodName + " ' was found.");
		printer.print(messages, command);
	}

	/*
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
	 */
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