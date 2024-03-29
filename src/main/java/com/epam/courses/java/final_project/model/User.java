package com.epam.courses.java.final_project.model;

import java.util.Objects;

/**
 * The {@code User} class represent corresponding entity from database.
 * {@code User} entity can have only one role, which define abilities of this account.
 * <p>
 * ~ {@code Customer} role - base role of any {@code User}.
 * <p>
 * ~ {@code Manager} role gives ability to accept and create responses for {@code Request}'s.
 * <p>
 *
 * @author Kostiantyn Kolchenko
 */
public class User {

    long id;  // value ether equal to corresponding db value, or 0
    String email;
    String password;
    String name;
    String surname;
    String phoneNumber;
    String verification;
    Role role;

    public User() {
        role = Role.Customer;
    }

    // Special constructor for creating entity from db
    public User(long id, String email, String password, String name, String surname, String phoneNumber, String verification, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.verification = verification;
        this.role = role;
    }

    public User(String email, String password, String name, String surname, String phoneNumber, String verification) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.verification = verification;
        role = Role.Customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleName() {
        return role.name();
    }

    public void setRole(int value) {
        this.role = Role.getRole(value);
    }

    /**
     * Enum for user role. Have special getter for getting corresponding value from db.
     */
    public enum Role {
        Customer(1), Manager(2);

        int dbValue;

        Role(int num) {
            dbValue = num;
        }

        public int getValue() {
            return dbValue;
        }

        public static Role getRole(int num) {
            if (num == 2)
                return Manager;
            return Customer;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email) && password.equals(user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(phoneNumber, user.phoneNumber) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, surname, phoneNumber, role);
    }
}
