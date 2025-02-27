package star.cafekiosk.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import star.cafekiosk.api.controller.dto.OrderCreateRequest;
import star.cafekiosk.api.service.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService service;

    @PostMapping("/orders")
    public void create(OrderCreateRequest request) {
        service.create(request, LocalDateTime.now());
    }


}
