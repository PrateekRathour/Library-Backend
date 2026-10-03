package com.library.service;

import com.library.dto.IssueRequest;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Fine;
import com.library.entity.User;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;

    @Value("${library.rules.default-borrow-days:14}")
    private int defaultBorrowDays;

    @Value("${library.rules.max-borrow-limit:5}")
    private int maxBorrowLimit;

    @Value("${library.rules.fine-per-day:10.00}")
    private double finePerDay;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository,
                         FineRepository fineRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.fineRepository = fineRepository;
    }

    @Transactional
    public BorrowRecord issueBook(IssueRequest request) {
        User member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMemberId()));

        if (!"ACTIVE".equalsIgnoreCase(member.getStatus())) {
            throw new BadRequestException("Member account is suspended or inactive. Cannot issue books.");
        }

        Long activeLoans = borrowRecordRepository.countByMemberIdAndStatus(member.getId(), "ISSUED");
        if (activeLoans >= maxBorrowLimit) {
            throw new BadRequestException("Member has reached maximum allowed active loans (" + maxBorrowLimit + ")");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("Book '" + book.getTitle() + "' is currently out of stock");
        }

        String currentOperator = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        int duration = (request.getDays() != null && request.getDays() > 0) ? request.getDays() : defaultBorrowDays;

        BorrowRecord record = new BorrowRecord();
        record.setMember(member);
        record.setBook(book);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(duration));
        record.setIssuedBy(currentOperator);
        record.setStatus("ISSUED");
        record.setRemarks(request.getRemarks());

        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord returnBook(Long recordId, String remarks) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + recordId));

        if ("RETURNED".equalsIgnoreCase(record.getStatus())) {
            throw new BadRequestException("Book is already marked as returned");
        }

        LocalDate returnDate = LocalDate.now();
        record.setReturnDate(returnDate);
        record.setStatus("RETURNED");
        if (remarks != null && !remarks.trim().isEmpty()) {
            record.setRemarks((record.getRemarks() != null ? record.getRemarks() + " | " : "") + remarks);
        }

        // Return book to inventory
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Check if overdue and calculate fine
        if (returnDate.isAfter(record.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
            if (overdueDays > 0) {
                double amount = overdueDays * finePerDay;
                Fine fine = new Fine(record, record.getMember(), amount, finePerDay, (int) overdueDays);
                fineRepository.save(fine);
            }
        }

        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord renewBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + recordId));

        if (!"ISSUED".equalsIgnoreCase(record.getStatus())) {
            throw new BadRequestException("Cannot renew a book that is already returned");
        }

        if (LocalDate.now().isAfter(record.getDueDate())) {
            throw new BadRequestException("Overdue books cannot be renewed online. Please return and pay applicable fines.");
        }

        record.setDueDate(record.getDueDate().plusDays(defaultBorrowDays));
        record.setRemarks((record.getRemarks() != null ? record.getRemarks() + " | " : "") + "Renewed on " + LocalDate.now());

        return borrowRecordRepository.save(record);
    }

    public List<BorrowRecord> getAllBorrowRecords(String query, String status) {
        return borrowRecordRepository.searchBorrowRecords(query, status);
    }

    public List<BorrowRecord> getMemberBorrowHistory(Long memberId) {
        return borrowRecordRepository.findByMemberIdOrderByIssueDateDesc(memberId);
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository.findByStatusOrderByIssueDateDesc("ISSUED");
    }

    public List<BorrowRecord> getOverdueBorrows() {
        return borrowRecordRepository.findOverdueRecords(LocalDate.now());
    }

    public BorrowRecord getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + id));
    }
}
