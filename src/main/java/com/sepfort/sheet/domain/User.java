package com.sepfort.sheet.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    /** @noinspection checkstyle:JavadocVariable*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /** @noinspection checkstyle:JavadocVariable*/
    private String username;
    /** @noinspection checkstyle:JavadocVariable*/
    private String password;
    /** @noinspection checkstyle:JavadocVariable*/
    private boolean active;

    /** @noinspection checkstyle:LineLength, checkstyle:JavadocVariable */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

//<editor-fold defaultstate="collapsed" desc="getters and setters">

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public Integer getId() {
        return id;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public String getUsername() {
        return username;
    }

    /** @noinspection checkstyle:DesignForExtension*/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** @noinspection checkstyle:DesignForExtension*/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** @noinspection checkstyle:DesignForExtension*/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** @noinspection checkstyle:DesignForExtension*/
    @Override
    public boolean isEnabled() {
        return isActive();
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @noinspection checkstyle:DesignForExtension*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public String getPassword() {
        return password;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public boolean isActive() {
        return active;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setActive(boolean active) {
        this.active = active;
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public Set<Role> getRoles() {
        return roles;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    //</editor-fold>
}
