package com.test.orderservice.service;

import com.test.orderservice.common.Payment;
import com.test.orderservice.common.TransactionRequest;
import com.test.orderservice.common.TransactionResponse;
import com.test.orderservice.entity.Order;
import com.test.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String ENDPOINT_URL;

    public TransactionResponse saveOrder(TransactionRequest transactionRequest){
        Order order = transactionRequest.getOrder();
        Payment payment = transactionRequest.getPayment();
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getPrice());

        // Rest api call for payment service
        Payment paymentResponse = restTemplate.postForObject(ENDPOINT_URL, payment, Payment.class);

        String resMsg = paymentResponse.getPaymentStatus().equalsIgnoreCase("SUCESS")?"Payment done and order placed ":
                "Payment failed, order cancelled";

        orderRepository.save(order);

        return new TransactionResponse(order,paymentResponse.getTransactionId(),payment.getAmount(),resMsg);
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

}
