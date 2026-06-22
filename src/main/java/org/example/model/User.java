package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "users")
public class User {
    @Id
    @Column(name = "phone_number")
    private long phoneNumber;
    @Column(name = "number_account")
    private int numberAccount;
    private String name;

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(int numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "phoneNumber=" + phoneNumber +
                ", numberAccount=" + numberAccount +
                ", name='" + name + '\'' +
                '}';
    }
}
