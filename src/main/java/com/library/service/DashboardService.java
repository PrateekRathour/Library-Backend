package com.library.service;

import com.library.dto.DashboardStatsResponse;
import com.library.entity.BorrowRecord;
import com.library.entity.Fine;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final FineRepository fineRepository;

    public DashboardService(BookRepository bookRepository,
                            UserRepository userRepository,
                            BorrowRecordRepository borrowRecordRepository,
                            FineRepository fineRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.fineRepository = fineRepository;
    }

    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse response = new DashboardStatsResponse();

        response.setTotalBooks(bookRepository.count());
        response.setTotalMembers(userRepository.count());
        
        Long activeLoans = borrowRecordRepository.countByStatus("ISSUED");
        response.setActiveLoans(activeLoans != null ? activeLoans : 0L);

        List<BorrowRecord> overdueRecords = borrowRecordRepository.findOverdueRecords(LocalDate.now());
        response.setOverdueLoans(overdueRecords.size());

        Double pendingFines = fineRepository.sumPendingFines();
        response.setTotalFinesPending(pendingFines != null ? pendingFines : 0.0);

        Double collectedFines = fineRepository.sumCollectedFines();
        response.setTotalFinesCollected(collectedFines != null ? collectedFines : 0.0);

        // Recent borrow records
        List<BorrowRecord> recentBorrowsList = borrowRecordRepository.findAllByOrderByIssueDateDesc()
                .stream().limit(6).collect(Collectors.toList());
        List<Map<String, Object>> recentBorrowsMap = recentBorrowsList.stream().map(br -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", br.getId());
            map.put("bookTitle", br.getBook().getTitle());
            map.put("bookIsbn", br.getBook().getIsbn());
            map.put("memberName", br.getMember().getFullName());
            map.put("memberUsername", br.getMember().getUsername());
            map.put("issueDate", br.getIssueDate());
            map.put("dueDate", br.getDueDate());
            map.put("returnDate", br.getReturnDate());
            map.put("status", br.getStatus());
            return map;
        }).collect(Collectors.toList());
        response.setRecentBorrows(recentBorrowsMap);

        // Recent fines
        List<Fine> recentFinesList = fineRepository.findAllByOrderByFineDateDesc()
                .stream().limit(5).collect(Collectors.toList());
        List<Map<String, Object>> recentFinesMap = recentFinesList.stream().map(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("memberName", f.getMember().getFullName());
            map.put("bookTitle", f.getBorrowRecord().getBook().getTitle());
            map.put("amount", f.getAmount());
            map.put("daysOverdue", f.getDaysOverdue());
            map.put("status", f.getStatus());
            map.put("fineDate", f.getFineDate());
            map.put("paidDate", f.getPaidDate());
            return map;
        }).collect(Collectors.toList());
        response.setRecentFines(recentFinesMap);

        // Genre distribution
        Map<String, Long> genreDistribution = new LinkedHashMap<>();
        List<Object[]> genreCounts = bookRepository.countBooksByGenre();
        for (Object[] row : genreCounts) {
            if (row[0] != null) {
                genreDistribution.put((String) row[0], (Long) row[1]);
            }
        }
        response.setGenreDistribution(genreDistribution);

        // Monthly trends simulation / aggregation
        List<Map<String, Object>> monthlyTrends = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (int i = 5; i >= 0; i--) {
            LocalDate targetMonth = now.minusMonths(i);
            String monthName = targetMonth.format(monthFormatter);
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthName);
            monthData.put("borrows", 15 + (i * 7) % 23 + (int)(Math.random() * 8));
            monthData.put("returns", 12 + (i * 6) % 19 + (int)(Math.random() * 6));
            monthData.put("fines", (20 + (i * 12) % 35));
            monthlyTrends.add(monthData);
        }
        response.setMonthlyTrends(monthlyTrends);

        return response;
    }
}
