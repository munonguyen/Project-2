package com.devon.building.service.impl;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.converter.CustomerConverter;
import com.devon.building.entity.Customer;
import com.devon.building.enums.Status;
import com.devon.building.exception.DataInvalidException;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;
    private final UserRepository userRepository;

    @Override
    public Customer sendDemand(CustomerDTO customerDTO) {
        if (customerRepository.existsByPhoneNumberAndActiveTrue(customerDTO.getPhoneNumber())) {
            throw new DataInvalidException("Số điện thoại này đã tồn tại trong hệ thống");
        }

        Customer customer = customerConverter.toCustomer(customerDTO);
        customer.setStatus(String.valueOf(Status.CHUA_XY_LY));
        customer.setActive(true);

        return customerRepository.save(customer);
    }

    @Override
    public PaginationResult<CustomerSearchResponse> findCustomer(CustomerSearchRequest customerSearchRequest, int page, int maxPageItem, int maxNavigationPage) {
        CustomerSearchBuilder customerSearchBuilder = customerConverter.toCustomerSearchBuilder(customerSearchRequest);
        PaginationResult<Customer> customers = customerRepository.findAllCustomer(customerSearchBuilder, page, maxPageItem, maxNavigationPage);
        return customerConverter.toPaginationResult(customers);
    }

    @Override
    @Transactional
    public ResponseDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByPhoneNumberAndActiveTrue(customerDTO.getPhoneNumber())) {
            throw new DataInvalidException("Số điện thoại này đã tồn tại trong hệ thống");
        }

        Customer customer = customerConverter.toCustomer(customerDTO);
        customer.setActive(true);
        customerRepository.save(customer);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Tạo khách hàng thành công");
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getId() == null) {
            throw new InvalidRequestException("Phải có ID khách hàng mới cập nhật");
        }

        Customer customer = customerRepository.findByIdAndActiveTrue(customerDTO.getId())
                .orElseThrow(() -> new DataInvalidException("Khách hàng không tồn tại có ID: " + customerDTO.getId()));

        Customer existingCustomerWithPhone = customerRepository.findByPhoneNumberAndActiveTrue(customerDTO.getPhoneNumber());
        if (existingCustomerWithPhone != null && !existingCustomerWithPhone.getId().equals(customerDTO.getId())) {
            throw new DataInvalidException("Số điện thoại này đã được sử dụng bởi khách hàng khác");
        }

        customerConverter.updateCustomerEntity(customerDTO, customer);
        customerRepository.save(customer);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Cập nhật khách hàng thành công");
        responseDTO.setData(customerConverter.toCustomerDTO(customer));
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO deleteCustomer(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) {
            throw new InvalidRequestException("Không có ID khách hàng được cung cấp");
        }

        List<Customer> customers = customerRepository.findAllByIdInAndActiveTrue(ids);
        if (customers.size() != ids.size()) {
            throw new DataInvalidException("Một số khách hàng không tồn tại hoặc đã bị xóa");
        }

        // Xóa mềm: Chuyển active thành false
        customers.forEach(customer -> customer.setActive(false));
        customerRepository.saveAll(customers);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Xóa khách hàng thành công");
        return responseDTO;
    }

    @Override
    public CustomerDTO findById(Long id) {
        if (id == null) {
            throw new InvalidRequestException("ID không được để trống");
        }

        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DataInvalidException("Khách hàng không tồn tại có ID: " + id));

        return customerConverter.toCustomerDTO(customer);
    }

    @Override
    public ResponseDTO loadStaffs(Long customerId) {
        if (customerId == null) {
            throw new InvalidRequestException("ID không được để trống");
        }

        Customer customer = customerRepository.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new DataInvalidException("Khách hàng không tồn tại có ID: " + customerId));

        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
        java.util.Set<Long> assignmentStaffs = customer.getUser()
                .stream().map(User::getId).collect(java.util.stream.Collectors.toSet());
        List<StaffResponseDTO> staffResponses = new ArrayList<>();
        for (User staff : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(staff.getId());
            staffResponseDTO.setUserName(staff.getUserName());
            staffResponseDTO.setChecked(assignmentStaffs.contains(staff.getId()) ? "checked" : "");
            staffResponses.add(staffResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponses);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO assignmentCustomer(AssignCustomerDTO assignCustomerDTO) {
        if (assignCustomerDTO.getCustomerId() == null) {
            throw new InvalidRequestException("ID khách hàng không được để trống");
        }

        Customer customer = customerRepository.findByIdAndActiveTrue(assignCustomerDTO.getCustomerId())
                .orElseThrow(() -> new DataInvalidException("Không tìm thấy khách hàng"));

        List<User> staffs = new ArrayList<>();
        if (assignCustomerDTO.getStaffIds() != null && !assignCustomerDTO.getStaffIds().isEmpty()) {
            staffs = userRepository.findAllById(assignCustomerDTO.getStaffIds());
            if (staffs.size() != assignCustomerDTO.getStaffIds().size()) {
                throw new DataInvalidException("Nhân viên không tồn tại");
            }
        }

        customer.getUser().clear();
        customer.getUser().addAll(staffs);
        customerRepository.save(customer);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Giao khách hàng thành công");
        return responseDTO;
    }
}

