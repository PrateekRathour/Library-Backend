package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberIdOrderByIssueDateDesc(Long memberId);
    List<BorrowRecord> findByBookIdOrderByIssueDateDesc(Long bookId);
    List<BorrowRecord> findByStatusOrderByIssueDateDesc(String status);
    List<BorrowRecord> findAllByOrderByIssueDateDesc();

    Long countByStatus(String status);
    Long countByMemberIdAndStatus(Long memberId, String status);

    @Query("SELECT br FROM BorrowRecord br WHERE br.status = 'ISSUED' AND br.dueDate < :currentDate")
    List<BorrowRecord> findOverdueRecords(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId AND br.status = 'ISSUED'")
    List<BorrowRecord> findActiveBorrowsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT br FROM BorrowRecord br WHERE " +
           "(:status IS NULL OR :status = '' OR br.status = :status) AND " +
           "(:query IS NULL OR :query = '' OR " +
           "LOWER(br.book.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(br.member.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(br.member.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(br.book.isbn) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<BorrowRecord> searchBorrowRecords(@Param("query") String query, @Param("status") String status);
}
