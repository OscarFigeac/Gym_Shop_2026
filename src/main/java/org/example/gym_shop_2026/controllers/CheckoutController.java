package org.example.gym_shop_2026.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_shop_2026.entities.*;
import org.example.gym_shop_2026.persistence.OrderDAO;
import org.example.gym_shop_2026.persistence.OrderItemDAO;
import org.example.gym_shop_2026.persistence.paymentMethodDAO;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.PaymentService;
import org.example.gym_shop_2026.services.TransactionService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final PaymentService paymentService;
    private final BasketService basketService;
    private final UserService userService;
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final paymentMethodDAO paymentMethodDAO;

    @Autowired
    public CheckoutController(PaymentService paymentService,
                              BasketService basketService,
                              UserService userService,
                              OrderDAO orderDAO,
                              OrderItemDAO orderItemDAO,
                              paymentMethodDAO paymentMethodDAO) {
        this.paymentService = paymentService;
        this.basketService = basketService;
        this.userService = userService;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.paymentMethodDAO = paymentMethodDAO;
    }

    @GetMapping("/begin")
    public String showCheckoutPage(Model model, Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        Basket basket = basketService.getBasketForUser(user.getUser_id());

        List<paymentMethod> methods = paymentMethodDAO.getMethodsForUser(user.getUser_id());
        model.addAttribute("paymentMethods", methods);
        model.addAttribute("basket", basket);
        model.addAttribute("grandTotal", basketService.getBasketTotal(basket.getBasketId()));

        return "checkout";
    }

    @PostMapping("/charge")
    @ResponseBody
    public String preparePayment(Principal principal) throws Exception {
        User user = userService.findUser(principal.getName());

        return paymentService.initiatePayment(user.getUser_id());
    }

    @PostMapping("/confirm")
    @ResponseBody
    public String finalizeOrder(Principal principal) throws SQLException {
        User user = userService.findUser(principal.getName());
        Basket basket = basketService.getBasketForUser(user.getUser_id());

        Order order = Order.builder()
                .user_id(user.getUser_id())
                .totalAmount(basketService.getBasketTotal(basket.getBasketId()))
                .status("PAID")
                .build();
        int orderId = orderDAO.createOrder(order);

        for (BasketItem item : basket.getItems()) {
            OrderItem oi = OrderItem.builder()
                    .order_id(orderId)
                    .product_id(item.getProductId())
                    .quantity(item.getItemQuantity())
                    .priceAtPurchase(item.getProduct().getPrice())
                    .build();
            orderItemDAO.createOrderItem(oi);
        }

        basketService.clearUserBasket(user.getUser_id());

        return "success";
    }

//    @PostMapping("/charge")
//    @ResponseBody
//    public String processPayment(@RequestBody PaymentRequest request, Principal principal) throws Exception {
//        User user = userService.findUser(principal.getName());
//        Basket basket = basketService.getBasketForUser(user.getUser_id());
//
//        String stripeId = paymentService.initiatePayment(user.getUser_id());
//
//        Order order = Order.builder()
//                .user_id(user.getUser_id())
//                .totalAmount(basketService.getBasketTotal(basket.getBasketId()))
//                .status("PAID")
//                .build();
//        int orderId = orderDAO.createOrder(order);
//
//        for (BasketItem item : basket.getItems()) {
//            OrderItem oi = OrderItem.builder()
//                    .order_id(orderId)
//                    .product_id(item.getProductId())
//                    .quantity(item.getItemQuantity())
//                    .priceAtPurchase(item.getProduct().getPrice())
//                    .build();
//            orderItemDAO.createOrderItem(oi);
//        }
//
//        basketService.clearUserBasket(user.getUser_id());
//
//        return stripeId;
//    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}