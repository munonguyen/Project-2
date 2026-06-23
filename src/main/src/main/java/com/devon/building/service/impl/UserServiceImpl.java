package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.UserService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listUserInfo(String key) {
        StringBuilder sql = new StringBuilder("SELECT NEW " + User.class.getName() + "(u.id, u.userName, u.active, u.userRole, u.fullName, u.phone) " + "FROM " + User.class.getName() + " u ");

        if (key != null && !key.trim().isEmpty()) {
            sql.append("WHERE (LOWER(u.userName) LIKE :key OR LOWER(u.fullName) LIKE :key OR LOWER(u.phone) LIKE :key) ");
        }

        sql.append("ORDER BY u.userName DESC");

        TypedQuery<User> query = entityManager.createQuery(sql.toString(), User.class);

        if (key != null && !key.trim().isEmpty()) {
            String searchKey = "%" + key.toLowerCase() + "%";
            query.setParameter("key", searchKey);
        }
        return query.getResultList();
    }

    @Override
    public void save(UserDTO userDTO) {
        String userName = userDTO.getUserName();
        User user = null;
        if (userName != null && !userName.isEmpty()) {
            user = userRepository.findByUserName(userName);
        }
        if (user != null) {
            throw new EntityExistsException("Người dùng có tên " + userName + " đã tồn tại");
        }
        user = new User();
        user.setUserName(userName);
        user.setActive(true);
        user.setFullName(userDTO.getFullName());
        user.setEncrytedPassword(passwordEncoder.encode(SystemConstant.PASSWORD_DEFAULT));
        user.setUserRole(User.ROLE_MANAGER);
        if (userDTO.getFileData() != null) {
            byte[] image = null;
            try {
                image = userDTO.getFileData().getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Invalid image data", e);
            }
            if (image != null && image.length > 0) {
                user.setImage(image);
            }
        }
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void update(UserDTO userDTO) {
        String userName = userDTO.getUserName();
        User user = null;
        if (userName != null && !userName.isEmpty()) {
            user = userRepository.findByUserName(userName);
        }
        if (user == null) {
            throw new EntityNotFoundException("Không tìm thấy thực thể có tên " + userName);
        }
        user.setUserName(userName);
        user.setActive(true);
        user.setUserRole(userDTO.getRoleCode());
        try {
            if (userDTO.getBase64Image() != null && !userDTO.getBase64Image().isEmpty()) {
                String base64String = userDTO.getBase64Image();
                if (base64String.contains(",")) {
                    base64String = base64String.split(",")[1];
                }

                byte[] imageBytes = Base64.getDecoder().decode(base64String);
                user.setImage(imageBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid image data", e);
        }
        userRepository.save(user);
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Optional<User> user = userRepository.findById(id);
            user.ifPresent(value -> value.setActive(false));
            userRepository.flush();
        }
    }

    @Override
    public Map<Long, String> loadStaffs() {
        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
        return staffs.stream().collect(Collectors.toMap(User::getId, User::getUserName));
    }
}
