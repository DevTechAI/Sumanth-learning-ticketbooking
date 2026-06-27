package com.TicketBooking.oops.client;

import com.TicketBooking.oops.dto.PaymentGatewayResponse;
import com.TicketBooking.oops.dto.PaymentVerificationRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayClient
{
    private final String keyId;
    private final String keySecret;
    private final String currency;

    public PaymentGatewayClient(
            @Value("${razorpay.key.id}") String keyId,
            @Value("${razorpay.key.secret}") String keySecret,
            @Value("${razorpay.currency:INR}") String currency)
    {
        this.keyId = keyId;
        this.keySecret = keySecret;
        this.currency = currency;
    }

    public PaymentGatewayResponse createOrder(int amountInRupees, String receipt) throws RazorpayException
    {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInRupees * 100);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order order = razorpayClient.orders.create(orderRequest);

        PaymentGatewayResponse response = new PaymentGatewayResponse();
        response.setSuccess(true);
        response.setKeyId(keyId);
        response.setOrderId(order.get("id"));
        response.setTransactionReference(order.get("id"));
        response.setAmount(order.get("amount"));
        response.setCurrency(order.get("currency"));
        response.setReceipt(order.get("receipt"));
        response.setStatus(order.get("status"));
        return response;
    }

    public boolean verifyPayment(PaymentVerificationRequest request) throws RazorpayException
    {
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", request.getRazorpayOrderId());
        attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
        attributes.put("razorpay_signature", request.getRazorpaySignature());
        return Utils.verifyPaymentSignature(attributes, keySecret);
    }

    public String getKeyId()
    {
        return keyId;
    }

    public String getCurrency()
    {
        return currency;
    }

}