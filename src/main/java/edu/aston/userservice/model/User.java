package edu.aston.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    private static final int MAX_NAME_LENGTH = 25;
    private static final int MAX_EMAIL_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(name = "email", nullable = false, length = MAX_EMAIL_LENGTH, unique = true)
    private String email;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User() {}

    public User(final String name, final String email, final int age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public User(final long id, final String name, final String email, final int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAge() {
        return this.age;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public boolean equals(final Object object) {
        if(object == null || this.getClass() != object.getClass()) {
            return false;
        }

        final User another = (User) object;

        return Objects.equals(this.id, another.id) &&
                Objects.equals(this.name, another.name) &&
                Objects.equals(this.email, another.email) &&
                Objects.equals(this.age, another.age) &&
                Objects.equals(this.createdAt, another.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.email, this.age, this.createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", name=" + this.name +
                ", email=" + this.email +
                ", age=" + this.age +
                ", createdAt=" + this.createdAt +
                "}";
    }
}
