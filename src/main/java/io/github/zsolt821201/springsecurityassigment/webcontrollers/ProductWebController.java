package io.github.zsolt821201.springsecurityassigment.webcontrollers;

import io.github.zsolt821201.springsecurityassigment.model.Product;
import io.github.zsolt821201.springsecurityassigment.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("products")
public class ProductWebController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ModelAndView getProducts() {
        ModelAndView modelAndView = new ModelAndView("products");
        modelAndView.addObject("productList", productService.getProducts());
        return modelAndView;

    }

    @GetMapping("/add")
    public ModelAndView addProduct() {
        ModelAndView modelAndView = new ModelAndView("add-product");
        modelAndView.addObject("product", new Product());
        return modelAndView;

    }

    @PostMapping("/add")
    public String addProduct(Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "add-product";
        productService.addProduct(product);
        return "redirect:/products";

    }

    @GetMapping("/delete/{code}")
    public String deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return "redirect:/products";

    }
}
