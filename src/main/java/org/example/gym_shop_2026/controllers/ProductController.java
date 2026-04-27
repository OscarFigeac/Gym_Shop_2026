package org.example.gym_shop_2026.controllers;

import org.example.gym_shop_2026.entities.Product;
import org.example.gym_shop_2026.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list"; // Refers to product-list.html
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String keyword, Model model) {
        List<Product> results = productService.searchProducts(keyword);
        model.addAttribute("products", results);
        model.addAttribute("keyword", keyword);
        return "product-list";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.getDetails(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "product-details";
    }

    @PostMapping("/buy")
    public String buyProduct(@RequestParam("productId") int productId,
                             @RequestParam("quantity") int quantity,
                             Model model) {
        boolean success = productService.purchaseProduct(productId, quantity);
        if (success) {
            model.addAttribute("message", "Purchase successful! Get ready for the gains.");
        } else {
            model.addAttribute("error", "Sorry, we're out of stock or there was an issue.");
        }
        return "purchase-status";
    }
}
