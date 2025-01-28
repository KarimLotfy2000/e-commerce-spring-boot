package com.e_commerce.repository;

 import jakarta.persistence.OrderColumn;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.e_commerce.entity.Order;

 import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
 List<Order> findAllByUserId(Long userId);
 List<Order> findTop5ByUserIdOrderByOrderDateDesc(Long userId);

}
