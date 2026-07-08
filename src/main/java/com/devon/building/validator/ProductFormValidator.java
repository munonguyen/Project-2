package com.devon.building.validator;

import com.devon.building.entity.Building;
import com.devon.building.form.ProductForm;
import com.devon.building.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProductFormValidator implements Validator {

  private final ProductRepository productRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz == ProductForm.class;
  }

  @Override
  public void validate(Object target, Errors errors) {
    ProductForm productForm = (ProductForm) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");
    Long id = productForm.getId();
    if (id != null) {
      if (id.toString().matches("\\s+")) {
        errors.rejectValue("code", "Pattern.productForm.code");
      } else if (productForm.isNewProduct()) {
        Building building = productRepository.findProduct(id);
        if (building != null) {
          errors.rejectValue("code", "Duplicate.productForm.code");
        }
      }
    }
  }
}
