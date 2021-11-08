import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseService {

	// static final String url = "jdbc:mysql://localhost:3306/store";
	// static final String JDBCDriver = "com.mysql.cj.jdbc.Driver";
	// static final String usernameDB = "root";
	// static final String password = "parola";

	Connection conn = null;
	Statement statement = null;

	public static Properties loadPropertiesFile() throws Exception {

		Properties properties = new Properties();
		InputStream input = new FileInputStream("databaseService.properties");
		properties.load(input);
		input.close();
		return properties;
	}

	
	public Connection createConnection() throws SQLException {

		try {

			Properties properties = loadPropertiesFile();

			String url = (String) properties.get("url");
			String JDBCDriver = (String) properties.get("driver");
			String usernameDB = (String) properties.get("username");
			String password = (String) properties.get("password");

			Class.forName(JDBCDriver);

			conn = DriverManager.getConnection(url, usernameDB, password);
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			System.out.println("connected");

		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;

	}

	public List<Product> getProductsByCategory(String categoryIn) throws Exception {

		List<Product> products = new ArrayList<Product>();
		String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
		
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(query)){
			while (result.next()) {
				Product product = new Product();
				if (result.getString("category").equals(categoryIn)) {
					product.setCategory(result.getString("category"));
					product.setQuantity(result.getLong("quantity"));
					product.setName(result.getString("namePr"));
					product.setPrice(result.getLong("price"));
					products.add(product);
				}

			}
		}
		return products;
	}

	public List<Product> getAllProducts() throws Exception {

		List<Product> products = new ArrayList<Product>();
		String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
		
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(query)){
			while (result.next()) {
				Product product = new Product();
				product.setCategoryID(result.getInt("CategoryID"));
				product.setCategory(result.getString("category"));
				product.setName(result.getString("namePr"));
				product.setQuantity(result.getLong("quantity"));
				product.setPrice(result.getInt("price"));
				product.setMaxQuantity(result.getLong("maxQuantity"));
				products.add(product);
			}
		
		}
		return products;
	}

	public static boolean isEmpty(Product product) {
		if (product == null) {
			return true;
		}
		return false;
	}
	
      Product isNull() {
		    return null;
		  }

	public Product getProductsByName(String nameIn) throws Exception {

		Product product;
		String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID ";
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(query)){
			product = new Product();
			
			while (result.next()) {
				if (result.getString("namePr").equals(nameIn)) {
					 product.setName(result.getString("namePr"));
					product.setCategoryID(result.getInt("CategoryID"));
					product.setQuantity(result.getLong("quantity"));
					product.setPrice(result.getLong("price"));
					product.setMaxQuantity(result.getLong("maxQuantity"));
					
				}
			}
		}
		return product;

	}

	public User getUserByName(String nameUser) throws Exception {

		User user = new User();
		String query = "SELECT *FROM clients";
		
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(query)) {
			while (result.next()) {
				if (result.getString("username").equals(nameUser)) {
					user.setUsername(result.getString("username"));
					user.setBalance(result.getLong("balance"));
				}
			}
		}

		return user;
	}

	public List<String> getProductsByCategory() throws Exception {
		List<String> categories = new ArrayList<String>();
		String query = "select *from categories";
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(query)){
			while (result.next()) {
				categories.add(result.getString("category"));
				categories.add(result.getString("CategoryID"));
			}
		}
		return categories;
	}

	public List<User> getUsers() throws Exception {

		List<User> clients = new ArrayList<User>();
		String queryClients = "select *from clients";
		try (Connection conn = createConnection();ResultSet result =statement.executeQuery(queryClients)) {
			while (result.next()) {
				User user = new User();

				user.setUsername(result.getString("username"));
				user.setBalance(result.getLong("balance"));

				clients.add(user);
			}
		}

		return clients;
	}

	public long decreaseQuantity(int quantityInp, String name) throws Exception {
		Product product = getProductsByName(name);

		long newQuantity = 0;

		if (product.getQuantity() > quantityInp) {
			newQuantity = product.getQuantity() - quantityInp;
		}

		return newQuantity;
	}

	public long decreaseBalance(int quantityInp, String nameUser, String name) throws Exception {
		User user = getUserByName(nameUser);
		Product product = getProductsByName(name);

		long newBalance = 0;

		long totalPrice = product.getPrice() * quantityInp;
		if (user.getBalance() > totalPrice) {
			newBalance = user.getBalance() - totalPrice;
		}
		return newBalance;
	}

	public void updateBuy(String prod, int quantityIn, String username) throws Exception {

		long q = decreaseQuantity(quantityIn, prod);
		long b = decreaseBalance(quantityIn, username, prod);
		
		String updateQueryProduct = "UPDATE product SET quantity=? WHERE namePr=?";
		String updateQueryClient = "UPDATE clients SET balance=? WHERE username=?";


		try (Connection conn = createConnection();PreparedStatement psUpdateProduct = conn.prepareStatement(updateQueryProduct);
				PreparedStatement psUpdateClient = conn.prepareStatement(updateQueryClient);){
		
			System.out.println(q);
			System.out.println(b);

			psUpdateProduct.setLong(1, q);
			psUpdateProduct.setString(2, prod);

			psUpdateClient.setLong(1, b);
			psUpdateClient.setString(2, username);

			psUpdateProduct.executeUpdate();
			psUpdateClient.executeUpdate();

			conn.commit();
		}

	}

	public long replenishQuantity(String prod, long quantity) throws Exception {

		Product product = getProductsByName(prod);
		String sqlUpdate = "UPDATE product " + "SET quantity = ? " + "WHERE namePr = ?";
		
		long newQuantity = 0;
		try (Connection conn = createConnection();PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)){
			newQuantity = product.getQuantity() + quantity;

			if (newQuantity <= product.getMaxQuantity()) {
				psUpdate.setLong(1, newQuantity);
				psUpdate.setString(2, prod);
				psUpdate.executeUpdate();
				conn.commit();
			}
		}
		return newQuantity;

	}

	public void addNewCategory(String category) throws Exception {

		String queryInsert = "insert into categories (category) values (?)";
		try (Connection conn = createConnection();PreparedStatement psInsert = conn.prepareStatement(queryInsert);){
			
			psInsert.setString(1, category);
			psInsert.executeUpdate();
			conn.commit();
		}
	}

	public void remove(String name) throws Exception {
		
		String queryDelete = "delete from product where namePr=?";
		
		try (Connection conn = createConnection();PreparedStatement psDelete = conn.prepareStatement(queryDelete)) {
			psDelete.setString(1, name);
			psDelete.executeUpdate();
			conn.commit();
		}

	}

	public void addProducts(int categoryid, String prodName, String categoryIn, String quantityIn, String priceIn)
			throws Exception {

		List<Product> products = getAllProducts();
		String queryInsert = "INSERT INTO product (CategoryID,namePr, quantity, price) VALUES (?,?, ?, ?)";
		
		try (Connection conn = createConnection();PreparedStatement psInsert = conn.prepareStatement(queryInsert)) {
			for (Product product : products) {
				if ((product.getCategory().equals(categoryIn))) {
					categoryid = product.getCategoryID();
				}
			}

			psInsert.setInt(1, categoryid);
			psInsert.setString(2, prodName);
			psInsert.setString(3, quantityIn);
			psInsert.setString(4, priceIn);
			psInsert.executeUpdate();
			conn.commit();
		}

	}

}
