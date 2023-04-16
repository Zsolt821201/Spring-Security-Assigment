package io.github.zsolt821201.springsecurityassigment.webcontrollers;

import io.github.zsolt821201.springsecurityassigment.exceptions.ForbiddenException;
import io.github.zsolt821201.springsecurityassigment.model.Product;
import io.github.zsolt821201.springsecurityassigment.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public ModelAndView addProduct(ModelAndView modelAndView, HttpServletRequest request) {
        try {
            if (request.isUserInRole("SUPERADMIN")) {
                modelAndView = new ModelAndView("add-product");
                modelAndView.addObject("product", new Product());
            } else {
                throw new ForbiddenException();
            }
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Something went wrong!!!");
            modelAndView.setViewName("/error");
        }
        return modelAndView;

    }

    @PostMapping("/add")
    public ModelAndView addProduct(@Valid Product product, BindingResult bindingResult, Model model, HttpServletRequest request) {
        try {
            if (request.isUserInRole("SUPERADMIN")) {
                if (bindingResult.hasErrors())
                    return new ModelAndView("add-product");
                productService.addProduct(product);
                return new ModelAndView("redirect:/products");

            } else {
                throw new ForbiddenException();
            }
        } catch (Exception e) {
            return new ModelAndView("/error", "errorMessage", "Something went wrong.");

        }

    }

    @GetMapping("/delete/{code}")
    public ModelAndView deleteProduct(@PathVariable String code, ModelAndView modelAndView, Authentication authentication, HttpServletRequest request) {
        try {
            modelAndView.addObject("productList", productService.getProducts());
            modelAndView.addObject("userName", authentication.getName());
            if(request.isUserInRole("SUPERADMIN")){
                productService.deleteProduct(code);
                modelAndView.setViewName("products");
            }
            else if(request.isUserInRole("ADMIN")){
                productService.deleteProduct(code);
                modelAndView.setViewName("products");
            }
            else{
                throw new ForbiddenException();
            }

        }catch (Exception e){
            modelAndView.addObject("errorMessage", "Something went wrong!!!");
            modelAndView.setViewName("/error");
            throw new ForbiddenException();

        }

        return new ModelAndView("redirect:/products");

    }
}
