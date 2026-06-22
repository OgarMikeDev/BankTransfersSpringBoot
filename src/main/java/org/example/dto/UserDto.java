package org.example.dto;

public class UserDto {
    private long phone;
    private int account;
    private String name;

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "phone=" + phone +
                ", account=" + account +
                ", name='" + name + '\'' +
                '}';
    }
}
