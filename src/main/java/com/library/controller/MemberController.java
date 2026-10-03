package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.MemberRequest;
import com.library.dto.MemberResponse;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers(
            @RequestParam(required = false) String query) {
        List<MemberResponse> members = memberService.getAllMembers(query);
        return ResponseEntity.ok(ApiResponse.ok("Members retrieved successfully", members));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long id) {
        MemberResponse member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.ok("Member retrieved successfully", member));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Member created successfully", member));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long id,
                                                                   @Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.updateMember(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Member updated successfully", member));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.ok("Member deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMemberStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        MemberResponse member = memberService.updateMemberStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Member status updated successfully", member));
    }
}
