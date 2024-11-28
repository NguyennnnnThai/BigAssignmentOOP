package org.example.bigassignment;

public class OverdueBorrow {
    private int borrowId;
    private int bookId;
    private String bookTitle;

    public OverdueBorrow(int borrowId, int bookId, String bookTitle) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}

