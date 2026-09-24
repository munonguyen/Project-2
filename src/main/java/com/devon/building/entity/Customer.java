package com.devon.building.entity;

import com.devon.building.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name="fullname",length=255,nullable=false)
    private String fullName;

    @Column(name="phone",length=255,nullable=false)
    private String phoneNumber;

    @Column(name="companyname")
    private String companyName;


    @Column(name="email",length=255)
    private String email;

    @Column(name="demand",length=255,nullable=false)
    private String demand;

    @Column(name="status",length=255,nullable=false)
    private String status ;

    @Column(name="is_active")
    private Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "assignmentcustomer",
            joinColumns = @JoinColumn(name = "customerid"),
            inverseJoinColumns = @JoinColumn(name = "staffid")
    )
    private List<User> user = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = true;
        }
    }
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
