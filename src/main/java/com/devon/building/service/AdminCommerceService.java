package com.devon.building.service;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.form.ProductForm;
import com.devon.building.model.OrderDetailInfo;
import com.devon.building.model.OrderInfo;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.impl.OrderRepository;
import com.devon.building.repository.impl.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommerceService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public PaginationResult<OrderInfo> listOrders(int page, int maxResult, int maxNavigationPage) {
        return orderRepository.listOrderInfo(page, maxResult, maxNavigationPage);
    }

    @Transactional(readOnly = true)
    public ProductForm getProductForm(Long id) {
        if (id == null) {
            ProductForm form = new ProductForm();
            form.setNewProduct(true);
            return form;
        }

        BuildingEntity building = productRepository.findProduct(id);
        if (building == null) {
            ProductForm form = new ProductForm();
            form.setNewProduct(true);
            return form;
        }
        return new ProductForm(building);
    }

    @Transactional
    public void saveProduct(ProductForm productForm) {
        productRepository.save(productForm);
    }

    @Transactional(readOnly = true)
    public OrderInfo getOrderWithDetails(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return null;
        }
        OrderInfo orderInfo = orderRepository.getOrderInfo(orderId);
        if (orderInfo == null) {
            return null;
        }
        List<OrderDetailInfo> details = orderRepository.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
        return orderInfo;
    }
}
