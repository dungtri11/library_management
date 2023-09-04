package com.example.demo.repository;

import com.example.demo.common.BorrowingStatus;
import com.example.demo.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {
    public Optional<BorrowingDetail> findByUserIdAndBookIdAndStatus(Long userid, Long bookid, BorrowingStatus status);
    public Optional<BorrowingDetail> findFirstByBookIdAndStatusOrderByBorrowTimeDesc(Long bookid, BorrowingStatus status);
}
