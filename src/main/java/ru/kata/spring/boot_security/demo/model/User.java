package ru.kata.spring.boot_security.demo.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "Укажите имя!")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2-х до 30 символов длинной")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2-х до 30 символов длинной")
    @NotBlank(message = "Укажите фамилию!")
    private String lastName;


    @Column(name = "email", nullable = false, length = 100)
    @NotBlank(message = "Укажите Email!")
    @Size(min = 10, max = 30, message = "Email должнен быть от 10 до 30 символов длинной")
    private String email;

    @Column(name = "password", nullable = false, length = 100, unique = true)
    @Size(min = 10, max = 100, message = "Пароль должен быть от 5 до 100 символов длинной")
    @NotBlank(message = "Укажите пароль!")
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public void addRoleToUser(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
