package com.example.cataloge.controller;

import java.util.List;

public class GetBooksInCartWrapper {
    private List<Long> bookIds;

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}