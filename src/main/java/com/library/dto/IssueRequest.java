package com.library.dto;

import jakarta.validation.constraints.NotNull;

public class IssueRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    private Integer days = 14;
    private String remarks;

    public IssueRequest() {}

    public IssueRequest(Long memberId, Long bookId, Integer days, String remarks) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.days = days;
        this.remarks = remarks;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
