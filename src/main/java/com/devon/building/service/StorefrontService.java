package com.devon.building.service;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.model.CartInfo;
import com.devon.building.model.ProductInfo;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.impl.OrderRepository;
import com.devon.building.repository.impl.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorefrontService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PaginationResult<ProductInfo> searchProducts(
            int page, int maxResult, int maxNavigationPage, String likeName) {
        return productRepository.queryProducts(page, maxResult, maxNavigationPage, likeName);
    }

    @Transactional(readOnly = true)
    public BuildingEntity findProduct(Long id) {
        if (id == null) {
            return null;
        }
        return productRepository.findProduct(id);
    }

    @Transactional
    public void placeOrder(CartInfo cartInfo) {
        orderRepository.saveOrder(cartInfo);
    }
}
