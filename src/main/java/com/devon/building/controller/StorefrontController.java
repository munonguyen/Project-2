package com.devon.building.controller;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.form.CustomerForm;
import com.devon.building.model.CartInfo;
import com.devon.building.model.CustomerInfo;
import com.devon.building.model.ProductInfo;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.StorefrontService;
import com.devon.building.utils.Utils;
import com.devon.building.validator.CustomerFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StorefrontController {

    private final StorefrontService storefrontService;
    private final CustomerFormValidator customerFormValidator;

    @InitBinder("customerForm")
    public void initCustomerFormBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(customerFormValidator);
    }

    @RequestMapping("/productList")
    public String listProducts(
            Model model,
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 8;
        final int maxNavigationPage = 10;

        PaginationResult<ProductInfo> result = storefrontService.searchProducts(
                page, maxResult, maxNavigationPage, likeName);
        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping("/buyProduct")
    public String buyProduct(
            HttpServletRequest request,
            @RequestParam(value = "id", required = false) Long id) {
        BuildingEntity building = storefrontService.findProduct(id);
        if (building != null) {
            Utils.getCartInSession(request).addProduct(new ProductInfo(building), 1);
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping("/shoppingCartRemoveProduct")
    public String removeProduct(
            HttpServletRequest request,
            @RequestParam(value = "id", required = false) Long id) {
        BuildingEntity building = storefrontService.findProduct(id);
        if (building != null) {
            Utils.getCartInSession(request).removeProduct(new ProductInfo(building));
        }
        return "redirect:/shoppingCart";
    }

    @PostMapping("/shoppingCart")
    public String updateCartQuantity(
            HttpServletRequest request,
            @ModelAttribute("cartForm") CartInfo cartForm) {
        Utils.getCartInSession(request).updateQuantity(cartForm);
        return "redirect:/shoppingCart";
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        model.addAttribute("cartForm", cartInfo);
        model.addAttribute("myCart", cartInfo);
        return "shoppingCart";
    }

    @GetMapping("/shoppingCartCustomer")
    public String customerForm(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }

        model.addAttribute("customerForm", new CustomerForm(cartInfo.getCustomerInfo()));
        return "shoppingCartCustomer";
    }

    @PostMapping("/shoppingCartCustomer")
    public String saveCustomer(
            HttpServletRequest request,
            @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
            BindingResult result) {
        if (result.hasErrors()) {
            customerForm.setValid(false);
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.setCustomerInfo(new CustomerInfo(customerForm));
        return "redirect:/shoppingCartConfirmation";
    }

    @GetMapping("/shoppingCartConfirmation")
    public String reviewOrder(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo == null || cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }

        model.addAttribute("myCart", cartInfo);
        return "shoppingCartConfirmation";
    }

    @PostMapping("/shoppingCartConfirmation")
    public String placeOrder(HttpServletRequest request) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }

        try {
            storefrontService.placeOrder(cartInfo);
        } catch (RuntimeException e) {
            return "shoppingCartConfirmation";
        }

        Utils.removeCartInSession(request);
        Utils.storeLastOrderedCartInSession(request, cartInfo);
        return "redirect:/shoppingCartFinalize";
    }

    @GetMapping("/shoppingCartFinalize")
    public String finalizeOrder(HttpServletRequest request, Model model) {
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @GetMapping("/productImage")
    public void productImage(
            HttpServletResponse response,
            @RequestParam("id") Long id) throws IOException {
        BuildingEntity building = storefrontService.findProduct(id);
        if (building != null && building.getImage() != null) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.getOutputStream().write(building.getImage());
        }
        response.getOutputStream().close();
    }
}
