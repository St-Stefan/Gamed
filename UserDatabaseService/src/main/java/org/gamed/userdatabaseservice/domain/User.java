package org.gamed.userdatabaseservice.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pwd_hash;

    @Column(nullable = false)
    private boolean developer;

    @Column(nullable = false)
    private boolean premium;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime time_created;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime time_modified;

    public User() {}

    public User(String name, String email, String pwd_hash, boolean developer, boolean premium) {
        this.name = name;
        this.email = email;
        this.pwd_hash = pwd_hash;
        this.developer = developer;
        this.premium = premium;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwdHash() {
        return pwd_hash;
    }

    public void setPwdHash(String pwd_hash) {
        this.pwd_hash = pwd_hash;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public void setDeveloper(boolean developer) {
        this.developer = developer;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public LocalDateTime getTimeCreated() {
        return time_created;
    }

    public LocalDateTime getTimeModified() {
        return time_modified;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", developer=" + developer +
                ", premium=" + premium +
                ", time_created=" + time_created +
                ", time_modified=" + time_modified +
                '}';
    }
}