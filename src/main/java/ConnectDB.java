import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectDB {

	static final String url = "jdbc:mysql://localhost:3306/store";
	static final String JDBCDriver = "com.mysql.cj.jdbc.Driver";
	static final String usernameDB = "root";
	static final String password = "parola";

	Connection conn = null;
	Statement statement = null;

	public void createConnection() throws SQLException {
		try {
			Class.forName(JDBCDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, usernameDB, password);
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			System.out.println("connected");

		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		}

	}

	public List<Product> getProductsByCategory(String categoryIn) throws SQLException {

		List<Product> products = new ArrayList<Product>();

		try {
			createConnection();
			String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				Product product = new Product();
				product.setCategory(result.getString("category"));
				if (product.getCategory().equals(categoryIn)) {
					product.setQuantity(result.getLong("quantity"));
					product.setName(result.getString("namePr"));
					product.setPrice(result.getLong("price"));
					products.add(product);
				}

			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return products;
	}

	public List<Product> getAllProducts() throws SQLException {

		List<Product> products = new ArrayList<Product>();

		try {
			createConnection();
			String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";
			ResultSet result = statement.executeQuery(query);

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
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return products;
	}

	public Product getProductsByName(String nameIn) throws SQLException {

		Product product;
		try {
			createConnection();
			String query = "SELECT *FROM product INNER JOIN categories ON product.CategoryID=categories.CategoryID";

			ResultSet result = statement.executeQuery(query);

			product= new Product();
			while (result.next()) {
				if (result.getString("namePr").equals(nameIn)) {
					product.setName(result.getString("namePr"));
					product.setCategoryID(result.getInt("CategoryID"));
					product.setQuantity(result.getLong("quantity"));
					product.setPrice(result.getLong("price"));
					product.setMaxQuantity(result.getLong("maxQuantity"));
				}

			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return product;
	}

	public User getUserByName(String nameUser) throws SQLException {

		User user = new User();
		try {
			createConnection();
			String query = "SELECT *FROM clients";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				if (result.getString("username").equals(nameUser)) {
					user.setUsername(result.getString("username"));
					user.setBalance(result.getLong("balance"));

				}

			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return user;
	}

	public List<String> getProductsByCategory() throws SQLException {

		List<String> categories = new ArrayList<String>();

		try {
			createConnection();
			String query = "select *from categories";
			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				categories.add(result.getString("category"));
			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return categories;
	}

	public List<User> getUsers() throws SQLException {

		List<User> clients = new ArrayList<User>();
		try {
			createConnection();

			String queryClients = "select *from clients";

			ResultSet result = statement.executeQuery(queryClients);

			while (result.next()) {
				User user = new User();

				user.setUsername(result.getString("username"));
				user.setBalance(result.getLong("balance"));

				clients.add(user);
			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return clients;
	}

	public long decreaseQuantity(int quantityInp, String name) throws SQLException {

		Product product = getProductsByName(name);

		long newQuantity = 0;

		if (product.getQuantity() > quantityInp) {
			newQuantity = product.getQuantity() - quantityInp;

		}

		return newQuantity;

	}

	public long decreaseBalance(int quantityInp, String nameUser, String name) throws SQLException {

	

		User user = getUserByName(nameUser);
		Product product = getProductsByName(name);

		long newBalance = 0;

		long totalPrice = product.getPrice() * quantityInp;
		if (user.getBalance() > totalPrice) {

			newBalance = user.getBalance() - totalPrice;

		}

		return newBalance;
	}
	
	public void updateBuy(String prod, int quantityIn, String username) throws SQLException {

		long q = decreaseQuantity(quantityIn, prod);
		long b = decreaseBalance(quantityIn, username, prod);

		
		try {
			createConnection();

			String updateQueryProduct = "UPDATE product SET quantity=? WHERE namePr=?";
			String updateQueryClient = "UPDATE clients SET balance=? WHERE username=?";

			PreparedStatement psUpdateProduct = conn.prepareStatement(updateQueryProduct);
			PreparedStatement psUpdateClient = conn.prepareStatement(updateQueryClient);

			
			System.out.println(q);
			System.out.println(b);

			psUpdateProduct.setLong(1, q);
			psUpdateProduct.setString(2, prod);
			

			psUpdateClient.setLong(1, b);
			psUpdateClient.setString(2, username);

		
			psUpdateProduct.executeUpdate();
			psUpdateClient.executeUpdate();

			conn.commit();
			System.out.println("update");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	}

	

	public long replenishQuantity(String prod, long quantity) throws SQLException {

		Product product = getProductsByName(prod);
		long newQuantity = 0;
		try {
			createConnection();

			String sqlUpdate = "UPDATE product " + "SET quantity = ? " + "WHERE namePr = ?";

			PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

			newQuantity = product.getQuantity() + quantity;
			


			if(newQuantity<=product.getMaxQuantity()) {
			psUpdate.setLong(1, newQuantity);
			psUpdate.setString(2, prod);
			psUpdate.executeUpdate();
			conn.commit();
			
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return newQuantity;

	}

/////
	public void addNewCategory(String category) throws SQLException {

		try {
			createConnection();
			String queryInsert = "insert into categories (category) values (?)";
			PreparedStatement psInsert = conn.prepareStatement(queryInsert);
			psInsert.setString(1, category);
			psInsert.executeUpdate();
			conn.commit();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	//////
	public void remove(String name) throws SQLException {

		try {
			createConnection();

			String queryDelete = "delete from product where namePr=?";
			PreparedStatement psDelete = conn.prepareStatement(queryDelete);

			psDelete.setString(1, name);
			psDelete.executeUpdate();
			conn.commit();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	}

	public void addProducts(String prodName, String categoryIn, String quantityIn, String priceIn) throws SQLException {

		//Product product = getProductsByName(prodName);
		try {
			createConnection();

			String queryInsert = "INSERT INTO product (namePr, quantity, price) VALUES (?, ?, ?)";
			PreparedStatement psInsert = conn.prepareStatement(queryInsert);

			 

			// List<Product> products = getAllProducts();

			//if (product.getName().equals(prodName)) {
				//	System.out.println("The product already exists.");
					//printer.print(messages, command);
				//	return;
			//	}
			// for (Product product : products) {
			// if (product.getCategory().equals(categoryIn)) {
			// psInsert.setInt(1, product.getCategoryID());

			// }
			// }
			psInsert.setString(1, prodName);
			psInsert.setString(2, quantityIn);
			psInsert.setString(3, priceIn);

			psInsert.executeUpdate();
			conn.commit();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	
	}

}
