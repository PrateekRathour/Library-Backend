package com.library.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsResponse {
    private long totalBooks;
    private long availableBooks;
    private long totalMembers;
    private long activeLoans;
    private long overdueLoans;
    private double totalFinesPending;
    private double totalFinesCollected;
    private List<Map<String, Object>> recentBorrows;
    private List<Map<String, Object>> recentFines;
    private Map<String, Long> genreDistribution;
    private List<Map<String, Object>> monthlyTrends;

    public DashboardStatsResponse() {}

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(long availableBooks) {
        this.availableBooks = availableBooks;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public long getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(long activeLoans) {
        this.activeLoans = activeLoans;
    }

    public long getOverdueLoans() {
        return overdueLoans;
    }

    public void setOverdueLoans(long overdueLoans) {
        this.overdueLoans = overdueLoans;
    }

    public double getTotalFinesPending() {
        return totalFinesPending;
    }

    public void setTotalFinesPending(double totalFinesPending) {
        this.totalFinesPending = totalFinesPending;
    }

    public double getTotalFinesCollected() {
        return totalFinesCollected;
    }

    public void setTotalFinesCollected(double totalFinesCollected) {
        this.totalFinesCollected = totalFinesCollected;
    }

    public List<Map<String, Object>> getRecentBorrows() {
        return recentBorrows;
    }

    public void setRecentBorrows(List<Map<String, Object>> recentBorrows) {
        this.recentBorrows = recentBorrows;
    }

    public List<Map<String, Object>> getRecentFines() {
        return recentFines;
    }

    public void setRecentFines(List<Map<String, Object>> recentFines) {
        this.recentFines = recentFines;
    }

    public Map<String, Long> getGenreDistribution() {
        return genreDistribution;
    }

    public void setGenreDistribution(Map<String, Long> genreDistribution) {
        this.genreDistribution = genreDistribution;
    }

    public List<Map<String, Object>> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(List<Map<String, Object>> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }
}
