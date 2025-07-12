package org.springshop.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "end_users")
@Getter @Setter
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;
    
    private String password;

    private Boolean active;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "endUser", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Authority> authorities;
}
