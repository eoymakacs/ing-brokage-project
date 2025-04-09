package org.ing.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.ing.dto.OrderRequest;
import org.ing.entity.Order;
import org.ing.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * Create Order: Create a new order for a given customer, asset, side, size and
	 * price. Side can be BUY or SELL. Customer is a unique id for a customer. Asset
	 * is the name of the stock customer wants to buy. Size represents how many
	 * shares customer wants to buy. Price represents how much customer wants to pay
	 * for per share. Orders should be created with PENDING status.
	 * 
	 * @param orderRequest
	 * @return
	 * @throws BadRequestException 
	 */
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) throws BadRequestException {
		return ResponseEntity.ok(orderService.createOrder(request.getCustomerId(), request.getAssetName(),
				request.getSide(), request.getSize(), request.getPrice()));
	}

	/**
	 * List Orders: List orders for a given customer and date range. (you can add
	 * more filter if you want)
	 * 
	 * @param customerId
	 * @param start
	 * @param end
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Order>> listOrders(@RequestParam Long customerId, @RequestParam LocalDateTime start,
			@RequestParam LocalDateTime end) {
		return ResponseEntity.ok(orderService.getOrders(customerId, start, end));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Order>> searchOrders(@RequestParam(required = false) Long customerId, @RequestParam(required = false) String assetName, @RequestParam(required = false) LocalDateTime start,
			@RequestParam(required = false) LocalDateTime end) {
		return ResponseEntity.ok(orderService.getOrders(customerId, start, end));
	}

	/**
	 * Delete Order: Cancel a pending order. Cancel Order (only if status is
	 * PENDING). Other status orders cannot be deleted.
	 * 
	 * @param orderId
	 * @return
	 */
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Order canceled successfully");
	}
}
