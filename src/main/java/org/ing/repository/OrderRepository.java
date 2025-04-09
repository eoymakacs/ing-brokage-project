package org.ing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.ing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	//@Query("SELECT o FROM Order o WHERE o.user.id = :customerId")
    List<Order> findByUserIdAndCreateDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
    
    //@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(Long userId);
}