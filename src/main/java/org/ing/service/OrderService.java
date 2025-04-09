package org.ing.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.ing.entity.Asset;
import org.ing.entity.Order;
import org.ing.entity.Side;
import org.ing.entity.Status;
import org.ing.entity.User;
import org.ing.entity.UserRole;
import org.ing.exception.CustomerNotFoundException;
import org.ing.exceptions.InvalidUserRoleException;
import org.ing.repository.AssetRepository;
import org.ing.repository.OrderRepository;
import org.ing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AssetRepository assetRepository;

	public Order createOrder(Long customerId, String assetName, Side side, BigDecimal size, BigDecimal price)
			throws BadRequestException {
		User user = userRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException(customerId));

		// Check if role is CUSTOMER
		if (!user.getRole().equals(UserRole.CUSTOMER)) {
			throw new InvalidUserRoleException("Orders can only be created for customers.");
		}

		Asset assetTRY = assetRepository.findByCustomerIdAndAssetName(user.getId(), "TRY")
				.orElseThrow(() -> new BadRequestException("TRY asset not found for customer"));

		BigDecimal totalCost = size.multiply(price);

		if (side == Side.BUY) {
			// Check if customer has enough usable TRY
			if (assetTRY.getUsableSize().compareTo(totalCost) < 0) {
				throw new BadRequestException("Insufficient TRY balance to place the order.");
			}

			// Deduct the cost from usable TRY
			assetTRY.setUsableSize(assetTRY.getUsableSize().subtract(totalCost));
			assetRepository.save(assetTRY);
		} else if (side == Side.SELL) {
			// Check if customer has enough usable asset to sell
			Asset assetToSell = assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
					.orElseThrow(() -> new BadRequestException("Asset not found for selling."));

			if (assetToSell.getUsableSize().compareTo(size) < 0) {
				throw new BadRequestException("Insufficient asset amount to place sell order.");
			}

			// Deduct from usable asset
			assetToSell.setUsableSize(assetToSell.getUsableSize().subtract(size));
			assetRepository.save(assetToSell);
		}

		// Save the order
		Order order = new Order();
		order.setUser(user);
		order.setAssetName(assetName);
		order.setSide(side);
		order.setSize(size);
		order.setPrice(price);

		return orderRepository.save(order);
	}

	public List<Order> getOrders(Long customerId, LocalDateTime start, LocalDateTime end) {
		return orderRepository.findByUserIdAndCreateDateBetween(customerId, start, end);
	}
	
	public List<Order> searchOrders(Long customerId, String assetName, LocalDateTime start, LocalDateTime end) {
		return orderRepository.searchOrders(customerId, assetName,
			start, end);
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