package com.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mvc.model.ContactModel;
import com.mvc.model.ProductModel;
import com.mvc.service.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Home Page
    @GetMapping({"/", "/home"})
    public String showHomePage() {
        return "index";
    }

    // Show Add Product Form
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductModel());
        return "add-product";
    }

    @GetMapping("/contactus")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new ContactModel());
        return "contactus";
    }

    @GetMapping("/aboutus")
    public String showAboutUs() {
        return "aboutus";
    }

    @PostMapping("/submitContact")
    public String submitContact(@ModelAttribute("contact") ContactModel contact, Model model) {
        System.out.println("Contact received: " + contact.getName() + ", " + contact.getEmail());
        model.addAttribute("message", "✅ Your message has been sent successfully!");
        return "contactus";
    }

    // ================= Save Product =================
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") ProductModel productModel,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            // Upload image and save URL
            if (!imageFile.isEmpty()) {
                String imageUrl = saveImageToFolder(imageFile);
                productModel.setImageUrl(imageUrl);
            }

            productModel.setCreatedAt(LocalDateTime.now());
            productModel.setCreatedBy("Shubh");

            productService.saveProduct(productModel);
            redirectAttributes.addFlashAttribute("message", "✅ Product saved successfully!");
            return "redirect:/products";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "❌ Failed to save product! " + e.getMessage());
            return "redirect:/products";
        }
    }

    // ================= View Products =================
    @GetMapping("/products")
    public String viewAllProducts(Model model) {
        List<ProductModel> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "view-products";
    }

    // ================= Search by ID =================
    @GetMapping("/searchById")
    public String searchById(@RequestParam("id") Long id, Model model) {
        ProductModel product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("products", List.of(product));
            model.addAttribute("notFound", false);
        } else {
            model.addAttribute("products", List.of());
            model.addAttribute("notFound", true);
        }
        return "view-products";
    }

    // ================= Delete Product =================
    @GetMapping("/delete/{id}")
    public String deleteProductById(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProductById(id);
        redirectAttributes.addFlashAttribute("message", "✅ Product deleted successfully!");
        return "redirect:/products";
    }

    // ================= Edit Product =================
    @GetMapping("/edit/{id}")
    public String editProductById(@PathVariable("id") Long id, Model model) {
        ProductModel product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product";
    }

    // ================= Update Product =================
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") ProductModel product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                String imageUrl = saveImageToFolder(imageFile);
                product.setImageUrl(imageUrl);
            }

            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("message", "✅ Product updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "❌ Failed to update product! " + e.getMessage());
        }
        return "redirect:/products";
    }	 

    // ================= Helper Method to Save Image =================
    private String saveImageToFolder(MultipartFile imageFile) throws IOException {
        String uploadDir = "uploads/";

        // Create folder if it doesn't exist
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        // Generate unique file name
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save file
        Files.write(filePath, imageFile.getBytes());

        // Return relative URL to store in DB
        return "/" + uploadDir + fileName;
    }

}
