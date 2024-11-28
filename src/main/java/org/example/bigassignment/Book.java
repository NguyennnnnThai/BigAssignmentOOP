package org.example.bigassignment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private IntegerProperty id = new SimpleIntegerProperty(0); // Khởi tạo mặc định
    private StringProperty title;
    private StringProperty author;
    private StringProperty category;
    private IntegerProperty availableQuantity;
    private IntegerProperty totalQuantity;

    // Constructor đầy đủ
    public Book(int id, String title, String author, String category, int availableQuantity, int totalQuantity) {
        this.id.set(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.category = new SimpleStringProperty(category);
        this.availableQuantity = new SimpleIntegerProperty(availableQuantity);
        this.totalQuantity = new SimpleIntegerProperty(totalQuantity);
    }

    // Constructor không bao gồm ID (khi thêm sách mới)
    public Book(String title, String author, String category, int availableQuantity, int totalQuantity) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.category = new SimpleStringProperty(category);
        this.availableQuantity = new SimpleIntegerProperty(availableQuantity);
        this.totalQuantity = new SimpleIntegerProperty(totalQuantity);
    }

    // Getter và Setter cho ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getter và Setter khác
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public int getAvailableQuantity() {
        return availableQuantity.get();
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity.set(availableQuantity);
    }

    public int getTotalQuantity() {
        return totalQuantity.get();
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity.set(totalQuantity);
    }
}
