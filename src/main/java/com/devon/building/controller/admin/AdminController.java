package com.devon.building.controller.admin;

import com.devon.building.form.ProductForm;
import com.devon.building.model.OrderInfo;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.AdminCommerceService;
import com.devon.building.validator.ProductFormValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AdminController {

    private final AdminCommerceService adminCommerceService;
    private final ProductFormValidator productFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target != null && target.getClass() == ProductForm.class) {
            dataBinder.setValidator(productFormValidator);
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/accountInfo")
    public String accountInfo(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            model.addAttribute("userDetails", userDetails);
        }
        return "accountInfo";
    }

    @GetMapping("/admin/orderList")
    public String orderList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page;
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }
        final int maxResult = 5;
        final int maxNavigationPage = 10;

        PaginationResult<OrderInfo> paginationResult = adminCommerceService.listOrders(
                page, maxResult, maxNavigationPage);
        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    @GetMapping("/admin/building")
    public String product(
            Model model,
            @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("productForm", adminCommerceService.getProductForm(id));
        return "product";
    }

    @PostMapping("/admin/building")
    public String productSave(
            Model model,
            @ModelAttribute("productForm") @Validated ProductForm productForm,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product";
        }
        try {
            adminCommerceService.saveProduct(productForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause != null ? rootCause.getMessage() : e.getMessage();
            model.addAttribute("errorMessage", message);
            return "product";
        }
        return "redirect:/productList";
    }

    @GetMapping("/admin/order")
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = adminCommerceService.getOrderWithDetails(orderId);
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }
}
