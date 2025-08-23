package com.ecom.sale.controller;

import com.ecom.sale.dto.OrderDto;
import com.ecom.sale.dto.request.OrderRequest;
import com.ecom.sale.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create new order", description = "Creates a new order based on the provided product IDs and quantities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order successfully created",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
    }
    )
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "Update existing order", description = "Updates an existing order by ID. Old items will be replaced with new ones.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order successfully updated",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
    }
    )
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @Parameter(description = "ID of the order to update", example = "1")
            @PathVariable Long orderId,
            @RequestBody @Valid OrderRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @Operation(summary = "Delete order", description = "Deletes an order by ID. If order is NEW, product quantities are restored.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
    }
    )
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long orderId
    ) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel order", description = "Cancels an order by ID and restores product stock.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
    }
    )
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @Parameter(description = "ID of the order to delete", example = "1")
            @PathVariable Long orderId
    ) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get order by ID", description = "Retrieves detailed information about an order by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
    }
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @Parameter(description = "ID of the order to retrieve", example = "1")
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @Operation(summary = "Get all orders (paginated)", description = "Retrieves a paginated list of all orders.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getOrders(pageable));
    }
}
