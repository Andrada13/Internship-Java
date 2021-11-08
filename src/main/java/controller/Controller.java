package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import interfaces.CommandProcessingServiceInterface;
import model.Product;


@Component
@RestController
@RequestMapping(headers = "Accept=application/json")
public class Controller {

	private CommandProcessingServiceInterface commandProcessingService;

	@Autowired
	Controller(CommandProcessingServiceInterface commandProcessingService) {
		this.commandProcessingService = commandProcessingService;
	}

	////// 1,2,3
	@GetMapping("/products")
	public List<String> getProducts(@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "category") String category) throws Exception {

		if (name != null) {
			return commandProcessingService.printProductsByName(name);
		} else if (category != null) {
			return commandProcessingService.printProductsByCategory(category);
		}
		return commandProcessingService.printProducts();
	}

	///// 4
	@GetMapping("/categories")
	public List<String> getCategories() throws Exception {

		return commandProcessingService.printCategories();
	}

/////5
	@PutMapping(path="/products/{name}", consumes="application/json", produces="application/json")
	public List<String> updateProduct(@PathVariable("name") String name,@RequestBody Request buy) throws Exception {

		Long quantityProduct = buy.getProduct().getQuantity();
		String username = buy.getUser().getUsername();
		String quantity=String.valueOf(quantityProduct);  
		
		return commandProcessingService.buy(name, quantity, username);
	}

	////// 6
	@PutMapping("/products/{name}/{quantity}")
	public List<String> updateQuantityProduct(@PathVariable("name") String name,
			@PathVariable("quantity") String quantity) throws Exception {
		return commandProcessingService.replenishQuantity(name, quantity);
	}

	/////// 7
	@PostMapping("/categories/{newCategory}")
	public List<String> addNewCategory(@PathVariable("newCategory") String newCategory) throws Exception {

		return commandProcessingService.addCategory(newCategory);
	}

	////// 8
	@PostMapping(path="/products/{name}", consumes="application/json", produces="application/json")
	public List<String> addProduct(@PathVariable("name") String name,	@RequestBody Product product) throws Exception{
		String category = product.getCategory().getName();
		Long quantityProduct = product.getQuantity();
		Long priceProduct = product.getPrice();
	  
		String quantity=String.valueOf(quantityProduct);  
		String price=String.valueOf(priceProduct);  

		return commandProcessingService.addProduct(name, category, quantity, price);
	
	}

	///// 9
	@DeleteMapping("/products/{name}")
	public List<String> deleteProduct(@PathVariable("name") String name) throws Exception {

		return commandProcessingService.removeProduct(name);
	}
	
	
	@GetMapping("/help")
	public List<String> help() throws Exception {

		return commandProcessingService.help();
	}

}
