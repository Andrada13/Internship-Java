package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;
	@Column(name = "namePr")
	private String name;
	private Long quantity;
	private Long price;
	private Long maxQuantity;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	Category category;

	public Product() {

	}

	public Product(Integer id, String name, Long quantity, Long price, Long maxQuantity, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.maxQuantity = maxQuantity;
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Long maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Product [ProductID=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", maxQuantity=" + maxQuantity + ", category=" + category + "]";
	}
}


