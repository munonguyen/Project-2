package com.devon.building.controller.admin.customer;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.enums.Status;
import com.devon.building.enums.Transaction;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.CustomerService;
import com.devon.building.service.TransactionService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.devon.building.model.dto.CustomerDTO;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @GetMapping("/list")
    public ModelAndView getAllCustomers(@RequestParam(value = "page", defaultValue = "1") String pageStr,
                                        @ModelAttribute("customerSearchRequest") CustomerSearchRequest customerSearchRequest) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerList");

        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)) {
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            customerSearchRequest.setStaffId(user.getId());
        }

        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        customerSearchRequest.setPage(page);

        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject("statusMap", Status.getStatus());
        modelAndView.addObject("customerSearchRequest", customerSearchRequest);

        PaginationResult<CustomerSearchResponse> result = customerService.findCustomer(
                customerSearchRequest, customerSearchRequest.getPage(), SystemConstant.MAX_PAGE_ITEM, SystemConstant.MAX_NAVIGATION_PAGE
        );
        modelAndView.addObject("result", result);

        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditCustomer() {
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerEdit");
        modelAndView.addObject("customer", new CustomerDTO());
        modelAndView.addObject("statusMap", Status.getStatus());
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateCustomer(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/customerEdit");
        CustomerDTO customerDTO = customerService.findById(id);
        modelAndView.addObject("customer", customerDTO);
        modelAndView.addObject("statusMap", Status.getStatus());
        modelAndView.addObject("transaction", Transaction.getStatus());
        modelAndView.addObject("transactionCSKH",transactionService.findByCustomerIdAndCode(id,"CSKH"));
        modelAndView.addObject("transactionDDX",transactionService.findByCustomerIdAndCode(id,"DDX"));
        return modelAndView;
    }
}
