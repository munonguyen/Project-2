package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable{

    @Serial
    private static final long serialVersionUID = -2054386655979281969L;

    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    public static final String ROLE_USER = "ROLE_USER";


    @Column(name = "username", length = 255, nullable = false)
    private String userName;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "userrole", length = 20, nullable = false)
    private String userRole;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "fullname", length = 255)
    private String fullName;

    @Column(name = "phone", length = 10)
    private String phone;


    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @ManyToMany(mappedBy = "staffs", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Building> buildings = new ArrayList<>();

    public User(Long id, String userName, Boolean active, String userRole, String fullName, String phone) {
        this.id = id;
        this.userName = userName;
        this.active = active;
        this.userRole = userRole;
        this.fullName = fullName;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "[" + this.userName + "," + this.password + "," + this.userRole + "]";
    }

}
