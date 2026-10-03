package com.library.service;

import com.library.dto.MemberRequest;
import com.library.dto.MemberResponse;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final FineRepository fineRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(UserRepository userRepository,
                         BorrowRecordRepository borrowRecordRepository,
                         FineRepository fineRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.fineRepository = fineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberResponse> getAllMembers(String query) {
        List<User> users;
        if (query != null && !query.trim().isEmpty()) {
            users = userRepository.searchUsers(query.trim());
        } else {
            users = userRepository.findAll();
        }

        return users.stream().map(this::enrichMemberResponse).collect(Collectors.toList());
    }

    public MemberResponse getMemberById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return enrichMemberResponse(user);
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        String rawPassword = (request.getPassword() != null && !request.getPassword().trim().isEmpty())
                ? request.getPassword()
                : "Library@123";

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(rawPassword),
                request.getFullName(),
                request.getPhone(),
                request.getAddress(),
                request.getRole() != null ? request.getRole() : Role.ROLE_MEMBER
        );

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getMembershipExpiryDate() != null) {
            user.setMembershipExpiryDate(request.getMembershipExpiryDate());
        }

        User savedUser = userRepository.save(user);
        return enrichMemberResponse(savedUser);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already in use");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getMembershipExpiryDate() != null) {
            user.setMembershipExpiryDate(request.getMembershipExpiryDate());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return enrichMemberResponse(updatedUser);
    }

    @Transactional
    public void deleteMember(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public MemberResponse updateMemberStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        user.setStatus(status.toUpperCase());
        return enrichMemberResponse(userRepository.save(user));
    }

    private MemberResponse enrichMemberResponse(User user) {
        MemberResponse response = MemberResponse.fromEntity(user);
        Long activeCount = borrowRecordRepository.countByMemberIdAndStatus(user.getId(), "ISSUED");
        Double pendingFines = fineRepository.sumPendingFinesByMemberId(user.getId());
        response.setActiveBorrowsCount(activeCount != null ? activeCount : 0L);
        response.setPendingFinesAmount(pendingFines != null ? pendingFines : 0.0);
        return response;
    }
}
