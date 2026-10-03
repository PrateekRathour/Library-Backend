package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.PayFineRequest;
import com.library.entity.Fine;
import com.library.service.FineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Fine>>> getAllFines(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status) {
        List<Fine> fines = fineService.getAllFines(query, status);
        return ResponseEntity.ok(ApiResponse.ok("Fines retrieved successfully", fines));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<Fine>>> getMemberFines(@PathVariable Long memberId) {
        List<Fine> fines = fineService.getMemberFines(memberId);
        return ResponseEntity.ok(ApiResponse.ok("Member fines retrieved successfully", fines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Fine>> getFineById(@PathVariable Long id) {
        Fine fine = fineService.getFineById(id);
        return ResponseEntity.ok(ApiResponse.ok("Fine details retrieved successfully", fine));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<Fine>> payFine(
            @PathVariable Long id,
            @Valid @RequestBody PayFineRequest request) {
        Fine fine = fineService.payFine(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Fine paid successfully", fine));
    }

    @PostMapping("/{id}/waive")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Fine>> waiveFine(
            @PathVariable Long id,
            @RequestParam(required = false) String remarks) {
        Fine fine = fineService.waiveFine(id, remarks);
        return ResponseEntity.ok(ApiResponse.ok("Fine waived successfully", fine));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFineStats() {
        Map<String, Object> stats = fineService.getFineStats();
        return ResponseEntity.ok(ApiResponse.ok("Fine statistics retrieved", stats));
    }
}
