package mums.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mums.dao.Product;
import mums.dao.ProductDao;
import mums.model.ProductForm;
import mums.model.ProductModel;

@Controller
public class ProductResource {

	@Autowired
	ProductDao productDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome(Model model) {

		return init(model);
	}
	
    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public ModelAndView bill(@ModelAttribute("productForm")ProductForm product, 
      BindingResult result, ModelMap model) {
    	
    	List<ProductModel> products = product.getProducts();
    	List<ProductModel> order = initialOrder(products);    	
    	BigDecimal calculatePriceApplyDisccount = calculatePriceApplyDisccount(products);
    	model.addAttribute("msg", calculatePriceApplyDisccount);
    	model.addAttribute("products", order);
    	return new ModelAndView("result","productForm",order);
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView redirect(Model model) {
      
    	return init(model);
    }   
    
	private ModelAndView init(Model model) {
		List<Product> products = productDao.findAll();
		ProductForm productForm = convertProductsModel(products);
		model.addAttribute("products", productForm);
		return new ModelAndView("welcome","productForm",productForm);
	}    
    
	private BigDecimal calculatePriceApplyDisccount(List<ProductModel> products) {
		List<ProductModel> productsWithout3x2 = new ArrayList<ProductModel>(); 
    	BigDecimal price3x2 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);       	
    	BigDecimal priceMenu = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);
    	BigDecimal priceWithoutDisccount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);       	
       	
       	price3x2 = applyDisccount3x2(price3x2, productsWithout3x2, products);  

       	for (ProductModel p: productsWithout3x2)
       	{       		
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
       		}    
       		productsWithout3x2.add(p);	
    	}
		return price3x2;
	}
	
	private BigDecimal totalPrice(BigDecimal price3x2, BigDecimal priceMenu, BigDecimal priceWithoutDisccount) {
		return price3x2.add(priceMenu.multiply(new BigDecimal(0.8)).add(priceWithoutDisccount)).setScale(2, BigDecimal.ROUND_FLOOR);
	}

	private BigDecimal addProductWithoutDisccount(BigDecimal priceWithoutDisccount, ProductModel p) {
		priceWithoutDisccount = priceWithoutDisccount.add((p.getPrice().multiply(new BigDecimal(p.getUnit()))));
		return priceWithoutDisccount;
	}
}