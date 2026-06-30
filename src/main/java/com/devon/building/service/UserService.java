package com.devon.building.service;

import com.devon.building.entity.User;
import com.devon.building.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> listUserInfo(String key);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void delete(List<Long> ids);

    Map<Long, String> loadStaffs();

}
