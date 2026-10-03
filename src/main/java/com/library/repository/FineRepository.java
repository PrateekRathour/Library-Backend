package com.library.repository;

import com.library.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberIdOrderByFineDateDesc(Long memberId);
    List<Fine> findByStatusOrderByFineDateDesc(String status);
    List<Fine> findAllByOrderByFineDateDesc();
    Optional<Fine> findByBorrowRecordId(Long borrowRecordId);

    @Query("SELECT COALESCE(SUM(f.amount), 0.0) FROM Fine f WHERE f.status = 'PENDING'")
    Double sumPendingFines();

    @Query("SELECT COALESCE(SUM(f.amount), 0.0) FROM Fine f WHERE f.status = 'PAID'")
    Double sumCollectedFines();

    @Query("SELECT COALESCE(SUM(f.amount), 0.0) FROM Fine f WHERE f.member.id = :memberId AND f.status = 'PENDING'")
    Double sumPendingFinesByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT f FROM Fine f WHERE " +
           "(:status IS NULL OR :status = '' OR f.status = :status) AND " +
           "(:query IS NULL OR :query = '' OR " +
           "LOWER(f.member.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.member.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.borrowRecord.book.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Fine> searchFines(@Param("query") String query, @Param("status") String status);
}
