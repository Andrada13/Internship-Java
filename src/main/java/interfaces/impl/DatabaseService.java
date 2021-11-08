
package interfaces.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import interfaces.DatabaseServiceInterface;
import model.Category;
import model.Product;
import model.User;

@Component
public class DatabaseService implements DatabaseServiceInterface {
	
	private static final Logger logger = LogManager.getLogger(DatabaseService.class);

	private EntityManagerFactory entityManagerFactory;

	public DatabaseService() {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("databaseService");
	}

	public List<Product> getProductByCategory(String category) throws Exception {

		List<Product> products = new ArrayList<Product>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			products = entityManager
					.createQuery("SELECT p FROM Product p JOIN FETCH p.category c WHERE c.name = :category",
							Product.class)
					.setParameter("category", category).getResultList();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

		return products;

	}

	public List<Product> getAllProducts() throws Exception {

		List<Product> products = new ArrayList<Product>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			products = entityManager
					.createQuery("SELECT p FROM Product p JOIN FETCH p.category c WHERE p.category = c.id ",
							Product.class)
					.getResultList();
		} catch (NoResultException nre) {

			logger.error("The list of products is empty.");
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return products;
	}

	public Product getProductsByName(String name) throws Exception {

		Product product = new Product();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			product = entityManager.createQuery("SELECT p FROM Product p  WHERE p.name= :name", Product.class)
					.setParameter("name", name).getSingleResult();
		} catch (NoResultException nre) {

			logger.error("No existing product with this name.");
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}

		return product;
	}

	public User getUserByName(String username) throws Exception {

		User user = new User();

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException nre) {
			
			logger.error("No existing user with this username.");
			return null;
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}

		return user;
	}

	public List<String> getCategoriesName() throws Exception {

		List<String> categories = new ArrayList<String>();
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			categories = entityManager.createQuery("SELECT c.name FROM Category c", String.class).getResultList();

		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return categories;
	}

	public long decreaseQuantity(int quantity, String name) throws Exception {
		Product product = getProductsByName(name);

		long newQuantity = 0;

		if (product.getQuantity() > quantity) {
			newQuantity = product.getQuantity() - quantity;
		}

		return newQuantity;
	}

	public long decreaseBalance(int quantity, String username, String name) throws Exception {
		User user = getUserByName(username);
		Product product = getProductsByName(name);

		long newBalance = 0;

		long totalPrice = product.getPrice() * quantity;
		if (user.getBalance() > totalPrice) {
			newBalance = user.getBalance() - totalPrice;
		}
		return newBalance;
	}

	public void updateBuy(String name, int quantity, String username) throws Exception {

		long q = decreaseQuantity(quantity, name);
		long b = decreaseBalance(quantity, username, name);

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			entityManager.getTransaction().begin();

			Query updateQueryProduct = entityManager
					.createQuery("UPDATE Product p SET p.quantity = ?1 WHERE p.name = ?2");
			Query updateQueryClient = entityManager
					.createQuery("UPDATE User u SET u.balance = ?1 WHERE u.username = ?2");
			updateQueryProduct.setParameter(1, q);
			updateQueryProduct.setParameter(2, name);

			updateQueryClient.setParameter(1, b);
			updateQueryClient.setParameter(2, username);

			updateQueryProduct.executeUpdate();
			updateQueryClient.executeUpdate();
			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

	public long replenishQuantity(String name, long quantity) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Product product = getProductsByName(name);
		long newQuantity = 0;
		try {
			entityManager.getTransaction().begin();

			Query replenishUpdate = entityManager.createQuery("UPDATE Product p SET p.quantity = ?1 WHERE p.name = ?2");

			newQuantity = product.getQuantity() + quantity;
			if (newQuantity <= product.getMaxQuantity()) {
				replenishUpdate.setParameter(1, newQuantity);
				replenishUpdate.setParameter(2, name);

				replenishUpdate.executeUpdate();

				entityManager.getTransaction().commit();
			}
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
		return newQuantity;

	}

	public void addNewCategory(String newCategory) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();

			Category category = new Category();
			category.setName(newCategory);

			entityManager.persist(category);
			entityManager.getTransaction().commit();
		} finally {

			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	public void remove(String name) throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {

			entityManager.getTransaction().begin();

			Product product = getProductsByName(name);
			product = entityManager.find(Product.class, product.getId());

			entityManager.remove(product);
			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	public Integer getCategoryid(String name) throws Exception {

		Integer id;
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			id = entityManager
					.createQuery("SELECT c.id FROM Category c WHERE c.name = :name",
							Integer.class)
					.setParameter("name", name).getSingleResult();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

		return id;

	}

	public void addProducts(Category id, String name, String categoryInput, String quantityInput, String priceInput)
			throws Exception {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		try {
			Integer categoryid = getCategoryid(categoryInput);
			Product newProduct = new Product();

			entityManager.getTransaction().begin();
		
			id = entityManager.find(Category.class, categoryid);
		
			long quantity = Long.parseLong(quantityInput);
			long price = Long.parseLong(priceInput);
		
		
			newProduct.setCategory(id);
			newProduct.setName(name);
			newProduct.setQuantity(quantity);
			newProduct.setPrice(price);

			entityManager.persist(newProduct);

			entityManager.getTransaction().commit();

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

}

