package com.photowebsite.repository;

import com.photowebsite.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByBuyerUsernameOrSellerUsername(String buyerUsername, String sellerUsername, Pageable pageable);
    boolean existsByPhotoIdAndBuyerUsernameAndStatus(Long photoId, String buyerUsername, Payment.PaymentStatus status);
}