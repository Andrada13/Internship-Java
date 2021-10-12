package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CategoryID")
	private Integer categoryID;
	@Column(name = "category")
	private String categoryPr;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	public Category() {

	}

	public Category(Integer categoryID, String categoryPr, List<Product> products) {
		super();
		this.categoryID = categoryID;
		this.categoryPr = categoryPr;
		this.products = products;
	}

	public String getCategoryPr() {
		return categoryPr;
	}

	public void setCategoryPr(String categoryPr) {
		this.categoryPr = categoryPr;
	}

	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getCategory() {
		return categoryPr;
	}

	public void setCategory(String category) {
		this.categoryPr = category;
	}

	@Override
	public String toString() {
		return categoryPr;
	}

}
