package com.devon.building.service;

import com.devon.building.entity.User;
import com.devon.building.model.dto.PasswordDTO;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.model.dto.UserRegisterDTO;
import com.devon.building.pagination.PaginationResult;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing User operations.
 */
public interface UserService {

    PaginationResult<User> listUserInfo(String key, int page, int maxResult, int maxNavigationPage);

    User getUserInfo(String username);

    User getUserByUserName(String username);

    User getUserById(Long id);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void delete(List<Long> ids);

    Map<Long, String> loadStaff();

    User register(UserRegisterDTO userRegisterDTO);

    void updatePassword(Long id, PasswordDTO passwordDTO);
}
