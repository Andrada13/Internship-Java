package interfaces.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import interfaces.CommandProcessingServiceInterface;
import interfaces.DatabaseServiceInterface;
import interfaces.PrinterInterface;
import model.Category;
import model.Product;
import model.User;

@Component
public class CommandProcessingService implements CommandProcessingServiceInterface {

	private DatabaseServiceInterface databaseService;
	private PrinterInterface printer;

	@Autowired
	CommandProcessingService(DatabaseServiceInterface databaseService, PrinterInterface printer) {
		this.databaseService = databaseService;
		this.printer = printer;
	}

	public CommandProcessingService() {
	}

	private static final String PRINT = "PRINT";
	private static final String CATEGORIES = "CATEGORIES";
	private static final String HELP = "HELP";
	private static final String PRODUCTS = "PRODUCTS";
	private static final String ALL = "ALL";
	private static final String REPLENISH = "REPLENISH";
	private static final String REMOVE = "REMOVE";
	private static final String PRODUCT = "PRODUCT";
	private static final String CATEGORY = "CATEGORY";
	private static final String ADD = "ADD";
	private static final String NEW = "NEW";
	private static final String BUY = "BUY";
	private static final String FOR = "FOR";

	public void executeCommand(String[] command) throws Exception {
		ArrayList<String> messages = new ArrayList<>();

		if (command.length == 1 && command[0].equals(HELP)) {
			help();
			return;
		}
		if (command.length == 2) {
			if (command[0].equals(PRINT) && command[1].equals(CATEGORIES)) {
				printCategories();
				return;
			}
		}
		if (command.length == 3) {
			if (command[0].equals(PRINT) && command[1].equals(PRODUCTS) && command[2].equals(ALL)) {
				printProducts();
				return;
			}
			if (command[0].equals(PRINT) && command[1].equals(PRODUCTS)) {
				printProductsByName(command[2]);
				return;
			}
			if (command[0].equals(REPLENISH)) {
				replenishQuantity(command[1], command[2]);
				return;
			}
			if (command[0].equals(REMOVE) && command[1].equals(PRODUCT)) {
				removeProduct(command[2]);
				return;
			}
			 if (command[0].equals("SWITCH") && command[1].equals("DISPLAY_MODE")) {
			 switchDisplayMode(command[2], null);
			 return;
			 }

		}

		if (command.length == 4) {
			if (command[0].equals(PRINT) && command[1].equals(PRODUCTS) && command[2].equals(CATEGORY)) {
				printProductsByCategory(command[3]);
				return;
			}

			if (command[0].equals(ADD) && command[1].equals(NEW) && command[2].equals(CATEGORY)) {
				addCategory(command[3]);
				return;
			}
			 if (command[0].equals("SWITCH") && command[1].equals("DISPLAY_MODE")) {
			 switchDisplayMode(command[2], command[3]);
			 return;
			 }
		}
		if (command.length == 5 && command[0].equals(BUY) && command[3].equals(FOR)) {
			buy(command[1], command[2], command[4]);
			return;
		}

		if (command.length == 7 && (command[0].equals(ADD)) && (command[1].equals(NEW))
				&& (command[2].equals(PRODUCT))) {
			addProduct(command[3], command[4], command[5], command[6]);
			return;
		}

		messages.add("That command is not recognizes. Please try typing help for a list of available commands. ");
		printer.print(messages, command);

	}

	public List<String> printProductsByCategory(String category) throws Exception {
		String[] command = null;
		int j = 1;
		ArrayList<String> messages = new ArrayList<>();

		List<Product> products = databaseService.getProductByCategory(category);

		for (Product product : products) {
			messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + product.getPrice());
			j++;
		}
		if (products.isEmpty()) {
			messages.add("No category named ' " + category + " ' was found.");

		}
		printer.print(messages, command);

		return messages;
	}

	// comanda 2
	public List<String> printProducts() throws Exception {
		String[] command = null;
		int j = 1;
		ArrayList<String> messages = new ArrayList<>();

		List<Product> products = databaseService.getAllProducts();

		for (Product product : products) {
			messages.add(j + " " + product.getName() + " " + product.getQuantity() + " " + product.getCategory() + " "
					+ product.getPrice());
			j++;
		}
		printer.print(messages, command);

		return messages;
	}

//comanda 3
	public List<String> printProductsByName(String name) throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		Product product = databaseService.getProductsByName(name);

		if (product != null) {
			messages.add(product.getName() + " " + product.getQuantity() + " " + product.getPrice());
		} else {
			messages.add("No product named ' " + name + " ' was found.");
		}
		printer.print(messages, command);
		return messages;
	}

//comanda 4 
	public List<String> printCategories() throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		List<String> list = databaseService.getCategoriesName();

		Iterator<String> it = list.iterator();
		String s = "";
		while (it.hasNext()) {
			s = s + it.next();
			if (it.hasNext()) {
				s = s + ",";
			}
		}
		messages.add(s);

		if (list.isEmpty()) {
			messages.add("Categories don't exist. ");
		}
		printer.print(messages, command);
		return messages;
	}

//comanda 5
	public List<String> buy(String name, String quantity, String username) throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		Product product = databaseService.getProductsByName(name);
		User user = databaseService.getUserByName(username);

		if (!(product == null)) {
			int quantityInp;
			try {
				quantityInp = Integer.parseInt(quantity);
			} catch (NumberFormatException nfe) {
				messages.add("Error ! The number must be integer.");
				printer.print(messages, command);
				return messages;
			}
			if (!(Integer.parseInt(quantity) > 0)) {
				messages.add("Quantity must be positive.");
				printer.print(messages, command);
				return messages;
			} else {
				if (!(user == null)) {
					long totalPrice = product.getPrice() * quantityInp;

					if (product.getQuantity() == 0) {
						messages.add("The product " + product.getName() + " is no longer in stock.");
						printer.print(messages, command);
						return messages;
					}
					if (!(user.getBalance() > totalPrice)) {
						messages.add("Your balance is too low.");
						printer.print(messages, command);
						return messages;
					}
					if (product.getQuantity() < quantityInp) {
						messages.add("User " + user.getUsername() + " cannot buy " + quantity + " " + product.getName()
								+ " because there is only " + product.getQuantity() + " " + product.getName()
								+ " left. ");
						printer.print(messages, command);
						return messages;
					}

					databaseService.updateBuy(name, quantityInp, username);

					messages.add("User " + user.getUsername() + " has bought " + quantity + " " + product.getName());
					printer.print(messages, command);
					return messages;
				}

			}

			messages.add("Client not found.");
			printer.print(messages, command);
			return messages;

		}

		messages.add("Product not found.");
		printer.print(messages, command);

		return messages;
	}

	// comanda 6
	public List<String> replenishQuantity(String name, String quantity) throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		int quantityInp;
		try {
			quantityInp = Integer.parseInt(quantity);

		} catch (NumberFormatException nfe) {
			messages.add("Error ! The second argument must be integer.");
			printer.print(messages, command);
			return messages;
		}
		if (quantityInp <= 0) {
			messages.add("Enter a valid quantity.");
			printer.print(messages, command);
			return messages;
		}

		Product product = databaseService.getProductsByName(name);

		if (!(product == null)) {
			if (product.getQuantity() == product.getMaxQuantity()) {
				messages.add("The quantity is already full.");
				printer.print(messages, command);
				return messages;
			}

			long q = databaseService.replenishQuantity(name, quantityInp);

			if (q > product.getMaxQuantity()) {
				messages.add("The given quantity is bigger than the maximum quantity.");
				printer.print(messages, command);
				return messages;
			}
			messages.add("Quantity successfully added.");
			printer.print(messages, command);
			return messages;
		}

		messages.add("No product named ' " + name + " ' was found.");
		printer.print(messages, command);
		return messages;

	}

	// comanda 7
	public List<String> addCategory(String newCategory) throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();
		HashSet<String> arrCategories = new HashSet<>();

		List<String> list = databaseService.getCategoriesName();
		for (String category : list) {
			arrCategories.add(category);
		}

		if (arrCategories.add(newCategory)) {

			databaseService.addNewCategory(newCategory);

			messages.add("Category added. ");
			printer.print(messages, command);
			return messages;
		} else {
			messages.add("Category already exists.");
			printer.print(messages, command);
			return messages;

		}
	}

	// comanda 8
	public List<String> addProduct(String name, String category, String quantity, String price) throws Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		HashSet<String> arrCategories = new HashSet<>();
		List<String> list = databaseService.getCategoriesName();

		for (String categoryItem : list) {
			arrCategories.add(categoryItem);
		}

		int quantityInp;
		int priceInp;
		try {
			quantityInp = Integer.parseInt(quantity);
			priceInp = Integer.parseInt(price);

		} catch (NumberFormatException nfe) {
			messages.add("Error ! The number must be integer.");
			printer.print(messages, command);
			return messages;
		}

		if (!(quantityInp > 0 && priceInp > 0)) {
			messages.add("Quantity and price must be positive.");
			printer.print(messages, command);
			return messages;
		}
		Category id = null;
		Product product = databaseService.getProductsByName(name);

		if (product == null) {

			if (!arrCategories.contains(category)) {
				messages.add("The category does not exists.");
				printer.print(messages, command);
				return messages;

			} else {

				databaseService.addProducts(id, name, category, quantity, price);
				messages.add(quantity + " " + name + " " + "have been added to " + category + " " + "category.");
				printer.print(messages, command);
				return messages;
			}
		}
		messages.add("The product already exists.");
		printer.print(messages, command);
		return messages;

	}

	// comanda 9
	public List<String> removeProduct(String name) throws NumberFormatException, Exception {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();

		Product product = databaseService.getProductsByName(name);

		if (!(product == null)) {
			if (!(product.getQuantity() == 0)) {
				messages.add("Cannot remove " + product.getName() + " " + "because quantity is not zero. Quantity is "
						+ product.getQuantity());
				printer.print(messages, command);
				return messages;
			} else {
				databaseService.remove(name);
				messages.add("Product removed.");
				printer.print(messages, command);
				return messages;
			}
		}
		messages.add("No product named ' " + name + " ' was found.");
		printer.print(messages, command);

		return messages;

	}

	// bonus 1
	public List<String> help() {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();
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

		return messages;

	}

	// comanda 11
	private void switchDisplayMode(String newDisplayMode, String newPath) {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();
		printer.print(messages, command);
		printer.setDisplayMode(newDisplayMode);
		if (newPath != null) {
			printer.setPath(newPath.replace("\"", ""));
			return;
		}
		printer.setPath(null);
	}

	// comanda 10
	private void printDisplayMode() {
		String[] command = null;
		ArrayList<String> messages = new ArrayList<>();
		messages.add(printer.getDisplayMode());
		printer.print(messages, command);

	}
}


