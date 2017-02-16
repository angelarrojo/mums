package mums.model;

import java.math.BigDecimal;

public class ProductModel {

	private int idProduct;
	private String name;
	private BigDecimal price;
	private String category;
	private String quantity;
	private int unit;

	public ProductModel()
	{
	}
	
	public ProductModel(int idProduct, String name, BigDecimal price, String category, String quantity, int unit) {
		super();
		this.idProduct = idProduct;
		this.name = name;
		this.price = price;
		this.category = category;
		this.quantity = quantity;
		this.unit = unit;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	
}