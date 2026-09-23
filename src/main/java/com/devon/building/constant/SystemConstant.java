package com.devon.building.constant;

public class SystemConstant {
    private SystemConstant() {}

    public static final String ADMIN_HOME = "/admin/users/list";
    public static final String ALERT = "alert";
    public static final String MESSAGE_RESPONSE = "messageResponse";
    public static final String PASSWORD_DEFAULT = "123456";

    // Dùng cho Authority đầy đủ
    public static final String USER_ROLE = "ROLE_USER";
    public static final String MANAGER_ROLE = "ROLE_MANAGER";
    public static final String STAFF_ROLE = "ROLE_STAFF";

    // Dùng cho hasRole()
    public static final String MANAGER = "MANAGER";
    public static final String STAFF = "STAFF";
    public static final String USER = "USER";

    // Dùng cho phân trang
    public static final Integer MAX_PAGE_ITEM = 5;
    public static final Integer MAX_NAVIGATION_PAGE = 10;


}
