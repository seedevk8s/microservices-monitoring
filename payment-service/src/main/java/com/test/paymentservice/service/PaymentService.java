package com.test.paymentservice.service;

import com.test.paymentservice.entity.Payment;
import com.test.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment){
        payment.setPaymentStatus(paymentProcessing());
        payment.setTransactionId(UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }

    public String paymentProcessing(){
        // API call from 3rd party payment gate way like paypal, paytm
        return new Random().nextBoolean()?"SUCESS":"FAILURE";
    }

    public Payment findPaymentHistoryByOrderID(int orderID) {
        return  paymentRepository.findByOrderId(orderID);
    }
}
