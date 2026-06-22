package org.example.dto;

public class UserDto {
    private long phone;
    private int account;
    private String name;
    private boolean block = false;

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

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "phone=" + phone +
                ", account=" + account +
                ", name='" + name + '\'' +
                ", block=" + block +
                '}';
    }
}
