package com.library.service;

import com.library.dto.PayFineRequest;
import com.library.entity.Fine;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<Fine> getAllFines(String query, String status) {
        return fineRepository.searchFines(query, status);
    }

    public List<Fine> getMemberFines(Long memberId) {
        return fineRepository.findByMemberIdOrderByFineDateDesc(memberId);
    }

    public Fine getFineById(Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine record not found with id: " + id));
    }

    @Transactional
    public Fine payFine(Long fineId, PayFineRequest request) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found with id: " + fineId));

        if ("PAID".equalsIgnoreCase(fine.getStatus())) {
            throw new BadRequestException("Fine is already marked as paid");
        }

        if ("WAIVED".equalsIgnoreCase(fine.getStatus())) {
            throw new BadRequestException("Fine has already been waived");
        }

        fine.setStatus("PAID");
        fine.setPaidDate(LocalDate.now());
        fine.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "CARD");
        fine.setTransactionId(request.getTransactionId() != null && !request.getTransactionId().trim().isEmpty()
                ? request.getTransactionId()
                : "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return fineRepository.save(fine);
    }

    @Transactional
    public Fine waiveFine(Long fineId, String remarks) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found with id: " + fineId));

        if ("PAID".equalsIgnoreCase(fine.getStatus())) {
            throw new BadRequestException("Cannot waive a fine that has already been paid");
        }

        fine.setStatus("WAIVED");
        fine.setPaidDate(LocalDate.now());
        fine.setPaymentMethod("WAIVED");
        fine.setTransactionId("WAIVED-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());

        return fineRepository.save(fine);
    }

    public Map<String, Object> getFineStats() {
        Double pending = fineRepository.sumPendingFines();
        Double collected = fineRepository.sumCollectedFines();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPendingFines", pending != null ? pending : 0.0);
        stats.put("totalCollectedFines", collected != null ? collected : 0.0);
        return stats;
    }
}
