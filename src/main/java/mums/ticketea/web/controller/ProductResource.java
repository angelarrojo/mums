package mums.ticketea.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mums.ticketea.dao.Product;
import mums.ticketea.dao.ProductDao;
import mums.ticketea.model.ProductForm;
import mums.ticketea.model.ProductModel;

@Controller
public class ProductResource {

	private static final String DRINK = "DRINK";
	private static final String DESSERT = "DESSERT";
	private static final String PRINCIPAL = "PRINCIPAL";
	@Autowired
	ProductDao productDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(Model model) {

		List<Product> products = productDao.findAll();
		ProductForm productForm = convertProductsModel(products);
		model.addAttribute("products", productForm);
		return new ModelAndView("welcome","productForm",productForm);
	}
	
    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public ModelAndView bill(@ModelAttribute("productForm")ProductForm product, 
      BindingResult result, ModelMap model) {
    	
    	List<ProductModel> products = product.getProducts();
    	List<ProductModel> order = initialOrder(products);    	
    	BigDecimal calculatePriceApplyDisccount = calculatePriceApplyDisccount(model, products);
    	model.addAttribute("msg", calculatePriceApplyDisccount);
    	model.addAttribute("products", order);
    	return new ModelAndView("result","productForm",order);
    }

	private BigDecimal calculatePriceApplyDisccount(ModelMap model, List<ProductModel> products) {
		List<ProductModel> productsWithout3x2 = new ArrayList<ProductModel>(); 
    	BigDecimal price3x2 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);       	
    	BigDecimal priceMenu = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);
    	BigDecimal priceWithoutDisccount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);       	
       	
       	price3x2 = applyDisccount3x2(price3x2, productsWithout3x2, products);  
       	
       	int numberOfMenus = howManyCompleteMenu(productsWithout3x2);
       	int numberMenuPrincipal = numberOfMenus;
       	int numberMenuDessert = numberOfMenus;
       	int numberMenuDrink = numberOfMenus;

       	for (ProductModel p: productsWithout3x2)
       	{       		
       		if (numberMenuPrincipal > 0 && PRINCIPAL.equals(p.getCategory()))
       		{
           		for (int j=0; j<p.getUnit(); j++)
           		{       			
           			if (numberMenuPrincipal > 0)
           			{
           				numberMenuPrincipal = numberMenuPrincipal -1;
           				priceMenu = addMenuProduct(priceMenu, p);
           			}
           		}
       		}
       		else if (numberMenuDessert > 0 && DESSERT.equals(p.getCategory()))
       		{
           		for (int j=0; j<p.getUnit(); j++)
           		{       			
           			if (numberMenuDessert > 0)
           			{
           				priceMenu = addMenuProduct(priceMenu, p);
           				numberMenuDessert = numberMenuDessert -1;
           			}
           		}
       		}
       		else if (numberMenuDrink > 0 && DRINK.equals(p.getCategory()))
       		{
           		for (int j=0; j<p.getUnit(); j++)
           		{       			
           			if (numberMenuDrink > 0)
           			{
           				priceMenu = addMenuProduct(priceMenu, p);
           				numberMenuDrink = numberMenuDrink -1;
           			}
           		}           		
       		}
       		priceWithoutDisccount = addProductWithoutDisccount(priceWithoutDisccount, p);
       	}    	       	    	
    	return totalPrice(price3x2, priceMenu, priceWithoutDisccount);
	}

	private List<ProductModel> initialOrder(List<ProductModel> products) {
		List<ProductModel> order = new ArrayList<ProductModel>();
    	for (ProductModel p: products)
    	{
    		order.add(new ProductModel(p.getIdProduct(), p.getName(), p.getPrice(), p.getCategory(), p.getQuantity(), p.getUnit()));
    	}
    	return order;
	}

	private ProductForm convertProductsModel(List<Product> products) {
		List<ProductModel> productsForm = new ArrayList<ProductModel>();
		for(Product p: products)
		{
			ProductModel pmodel = new ProductModel();
			pmodel.setIdProduct(p.getIdProduct());
			pmodel.setName(p.getName());
			pmodel.setPrice(p.getPrice());
			pmodel.setCategory(p.getCategory());
			pmodel.setQuantity(p.getQuantity());
			pmodel.setUnit(0);
			productsForm.add(pmodel);
		}

		ProductForm productForm = new ProductForm();
		productForm.setProducts(productsForm);
		return productForm;
	}
	
	private int howManyCompleteMenu(List<ProductModel> products) {
		Map<String, Integer> menu = new HashMap<String, Integer>();
       	for(ProductModel p: products)
       	{
       		int count = menu.containsKey(p.getCategory()) ? menu.get(p.getCategory()) : 0;
       		menu.put(p.getCategory(), count + p.getUnit());
       	}
       	Entry<String, Integer> min = Collections.min(menu.entrySet(), new Comparator<Entry<String, Integer>>() {
       	    public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
       	        return entry1.getValue().compareTo(entry2.getValue());
       	    }
       	});
       	return min.getValue();
	}
	
	private BigDecimal applyDisccount3x2(BigDecimal price3x2, List<ProductModel> productsWithout3x2, List<ProductModel> products) {
		for(ProductModel p: products)
    	{
       		if (p.getUnit() != 0)
       		{
        		if (p.getUnit() > 2)
        		{
        			price3x2 = price3x2.add(p.getPrice().multiply( new BigDecimal((p.getUnit()/3)*2)));
        			p.setUnit(p.getUnit()%3);
        		}
        		productsWithout3x2.add(p);	
       		}       		
    	}
		return price3x2;
	}
	
	private BigDecimal totalPrice(BigDecimal price3x2, BigDecimal priceMenu, BigDecimal priceWithoutDisccount) {
		return price3x2.add(priceMenu.multiply(new BigDecimal(0.8)).add(priceWithoutDisccount)).setScale(2, BigDecimal.ROUND_FLOOR);
	}

	private BigDecimal addMenuProduct(BigDecimal priceMenu, ProductModel p) {
		p.setUnit(p.getUnit()-1);
		priceMenu = sumPriceProductWithMenu(priceMenu, p);
		return priceMenu;
	}

	private BigDecimal sumPriceProductWithMenu(BigDecimal priceMenu, ProductModel p) {
		priceMenu = priceMenu.add(p.getPrice());
		return priceMenu;
	}

	private BigDecimal addProductWithoutDisccount(BigDecimal priceWithoutDisccount, ProductModel p) {
		priceWithoutDisccount = priceWithoutDisccount.add((p.getPrice().multiply(new BigDecimal(p.getUnit()))));
		return priceWithoutDisccount;
	}
}