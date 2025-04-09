package org.ing.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.ing.entity.User;
import org.ing.entity.Order;
import org.ing.entity.Side;
import org.ing.entity.Status;
import org.ing.repository.UserRepository;
import org.ing.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long customerId, String assetName, Side side, BigDecimal size, BigDecimal price) {
        User user = userRepository.findById(customerId).orElseThrow();
        Order order = new Order();
        order.setUser(user);
        order.setAssetName(assetName);
        order.setSide(side);
        order.setSize(size);
        order.setPrice(price);
        order.setStatus(Status.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<Order> getOrders(Long customerId, LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByUserIdAndCreateDateBetween(customerId, start, end);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() == Status.PENDING) {
            order.setStatus(Status.CANCELED);
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Only pending orders can be canceled");
        }
    }
}