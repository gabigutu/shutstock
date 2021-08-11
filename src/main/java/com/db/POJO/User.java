package com.db.POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(50)", name = "username", nullable = false, unique = true)
    private String username;
    @Column(columnDefinition = "varchar(50)", name = "password", nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(50)", name = "email", nullable = false, unique = true)
    private String email;
    @Column(columnDefinition = "char", name = "role", nullable = false)
    private char role;
    @Column(name = "uid", nullable = false, unique = true)
    private String UID;

    @Column(name = "is_activated", columnDefinition="tinyint(1) default 0")
    private boolean isActivated;

    @Column(name = "login_token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwtToken;

}
