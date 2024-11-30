package org.example.bigassignment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Loan {
    private IntegerProperty id = new SimpleIntegerProperty(0); // ID của việc mượn
    private IntegerProperty bookId = new SimpleIntegerProperty(); // ID sách
    private IntegerProperty borrowerId = new SimpleIntegerProperty(); // ID người mượn
    private ObjectProperty<LocalDate> loanDate = new SimpleObjectProperty<>(); // Ngày mượn
    private ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>(); // Hạn trả
    private ObjectProperty<LocalDate> returnDate = new SimpleObjectProperty<>(); // Ngày trả thực tế
    private StringProperty status = new SimpleStringProperty("On Loan"); // Trạng thái (mặc định là "On Loan")
    private IntegerProperty fine = new SimpleIntegerProperty(0); // Tiền phạt (mặc định là 0)

    // Constructor đầy đủ
    public Loan(int id, int bookId, int borrowerId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, String status, int fine) {
        this.id.set(id);
        this.bookId.set(bookId);
        this.borrowerId.set(borrowerId);
        this.loanDate.set(loanDate);
        this.dueDate.set(dueDate);
        this.returnDate.set(returnDate);
        this.status.set(status);
        this.fine.set(fine);
    }

    // Constructor khi chưa trả sách
    public Loan(int bookId, int borrowerId, LocalDate loanDate, LocalDate dueDate) {
        this.bookId.set(bookId);
        this.borrowerId.set(borrowerId);
        this.loanDate.set(loanDate);
        this.dueDate.set(dueDate);
    }

    // Getter và Setter
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getBookId() {
        return bookId.get();
    }

    public void setBookId(int bookId) {
        this.bookId.set(bookId);
    }

    public int getBorrowerId() {
        return borrowerId.get();
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId.set(borrowerId);
    }

    public LocalDate getLoanDate() {
        return loanDate.get();
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate.set(loanDate);
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public LocalDate getReturnDate() {
        return returnDate.get();
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getFine() {
        return fine.get();
    }

    public void setFine(int fine) {
        this.fine.set(fine);
    }
}
