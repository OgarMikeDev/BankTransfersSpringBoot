package org.example.dto;

public class UserDto {
    private long phone;
    private String name;
    private int sum;
    private boolean block = false;

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "phone=" + phone +
                ", name='" + name + '\'' +
                ", sum=" + sum +
                ", block=" + block +
                '}';
    }
}
