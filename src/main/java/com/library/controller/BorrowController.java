package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.IssueRequest;
import com.library.dto.ReturnRequest;
import com.library.entity.BorrowRecord;
import com.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/issue")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<BorrowRecord>> issueBook(@Valid @RequestBody IssueRequest request) {
        BorrowRecord record = borrowService.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Book issued successfully", record));
    }

    @PostMapping("/return/{recordId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<BorrowRecord>> returnBook(
            @PathVariable Long recordId,
            @RequestBody(required = false) ReturnRequest request) {
        String remarks = request != null ? request.getRemarks() : null;
        BorrowRecord record = borrowService.returnBook(recordId, remarks);
        return ResponseEntity.ok(ApiResponse.ok("Book returned successfully", record));
    }

    @PostMapping("/renew/{recordId}")
    public ResponseEntity<ApiResponse<BorrowRecord>> renewBook(@PathVariable Long recordId) {
        BorrowRecord record = borrowService.renewBook(recordId);
        return ResponseEntity.ok(ApiResponse.ok("Book renewed successfully", record));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<BorrowRecord>>> getActiveBorrows() {
        List<BorrowRecord> records = borrowService.getActiveBorrows();
        return ResponseEntity.ok(ApiResponse.ok("Active borrow records retrieved", records));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<BorrowRecord>>> getOverdueBorrows() {
        List<BorrowRecord> records = borrowService.getOverdueBorrows();
        return ResponseEntity.ok(ApiResponse.ok("Overdue borrow records retrieved", records));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<BorrowRecord>>> getAllBorrowRecords(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status) {
        List<BorrowRecord> records = borrowService.getAllBorrowRecords(query, status);
        return ResponseEntity.ok(ApiResponse.ok("Borrow history retrieved", records));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<BorrowRecord>>> getMemberBorrowHistory(@PathVariable Long memberId) {
        List<BorrowRecord> records = borrowService.getMemberBorrowHistory(memberId);
        return ResponseEntity.ok(ApiResponse.ok("Member borrow history retrieved", records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowRecord>> getBorrowRecordById(@PathVariable Long id) {
        BorrowRecord record = borrowService.getBorrowRecordById(id);
        return ResponseEntity.ok(ApiResponse.ok("Borrow record retrieved", record));
    }
}
