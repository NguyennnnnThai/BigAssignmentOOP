package org.example.bigassignment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Borrower {
    private IntegerProperty id = new SimpleIntegerProperty(0); // Khởi tạo mặc định
    private StringProperty fullName;
    private IntegerProperty age;
    private StringProperty phoneNumber;

    // Constructor đầy đủ (khi lấy từ cơ sở dữ liệu)
    public Borrower(int id, String fullName, int age, String phoneNumber) {
        this.id.set(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.age = new SimpleIntegerProperty(age);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    // Constructor không bao gồm ID (dùng khi tạo người mượn mới)
    public Borrower(String fullName, int age, String phoneNumber) {
        this.id.set(0); // Giá trị mặc định cho id là 0
        this.fullName = new SimpleStringProperty(fullName);
        this.age = new SimpleIntegerProperty(age);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    // Getter và Setter cho ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getter và Setter cho fullName
    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    // Getter và Setter cho age
    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    // Getter và Setter cho phoneNumber
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    // Các phương thức Property để dùng cho binding trong JavaFX
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

}
