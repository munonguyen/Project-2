package com.devon.building.service;

import com.devon.building.entity.User;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.pagination.PaginationResult;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing User operations.
 */
public interface UserService {

    /**
     * Retrieves a paginated list of users based on a search key.
     *
     * @param key               the search keyword for filtering users
     * @param page              the current page number
     * @param maxResult         the maximum number of results per page
     * @param maxNavigationPage the maximum number of navigation pages to display
     * @return a {@link PaginationResult} containing the list of users
     */
    PaginationResult<User> listUserInfo(String key, int page, int maxResult, int maxNavigationPage);

    /**
     * Retrieves user information by their username.
     *
     * @param username the username of the user
     * @return the {@link User} entity
     */
    User getUserInfo(String username);

    /**
     * Saves a new user to the system.
     *
     * @param userDTO the data transfer object containing new user details
     */
    void save(UserDTO userDTO);

    /**
     * Updates an existing user's information.
     *
     * @param userDTO the data transfer object containing updated user details
     */
    void update(UserDTO userDTO);

    /**
     * Deletes (soft delete or hard delete) users based on their IDs.
     *
     * @param ids the list of user IDs to delete
     */
    void delete(List<Long> ids);

    /**
     * Loads a map of staff members.
     *
     * @return a Map where the key is the user ID and the value is the username
     */
    Map<Long, String> loadStaff();

    /**
     * Registers a new user from register request.
     *
     * @param userRegisterDTO the data transfer object containing register details
     * @return the created {@link User} entity
     */
    User register(com.devon.building.model.dto.UserRegisterDTO userRegisterDTO);



}
