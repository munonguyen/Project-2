package com.devon.building.repository;

import com.devon.building.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserName(String userName);

  void deleteByIdIn(List<Long> ids);

  List<User> findAllByUserRoleAndActiveTrue(String userRole);

  List<User> findAllByUserRoleInAndActiveTrue(List<String> userRoles);
}
