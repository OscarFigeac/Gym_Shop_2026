package org.example.gym_shop_2026.controllers;

import com.stripe.service.BalanceService;
import org.example.gym_shop_2026.entities.Basket;
import org.example.gym_shop_2026.entities.User;
import org.example.gym_shop_2026.services.BasketService;
import org.example.gym_shop_2026.services.OrderService;
import org.example.gym_shop_2026.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.SQLException;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final BasketService basketService;
    private final OrderService orderService;

    public DashboardController(UserService userService, BasketService basketService,
                               OrderService orderService) {
        this.userService = userService;
        this.basketService = basketService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) throws SQLException {
        User user = userService.findUser(userDetails.getUsername());
        Basket basket = basketService.getBasketForUser(user.getUser_id());

        int itemCount = (basket != null && basket.getItems() != null) ? basket.getItems().size() : 0;

        int activeOrders = orderService.countActiveOrdersForUser(user.getUser_id());

        String displayUserType = "Member";

        model.addAttribute("user", user);
        model.addAttribute("basketCount", itemCount);
        model.addAttribute("activeOrderCount", activeOrders);
        model.addAttribute("userType", displayUserType);

        return "dashboard";
    }

    @GetMapping("/settings")
    public String showSettings(Model model, @AuthenticationPrincipal UserDetails userDetails) throws SQLException {
        User user = userService.findUser(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile-settings";
    }

    @PostMapping("/2fa/toggle")
    public String toggle2fa(@AuthenticationPrincipal UserDetails userDetails) throws SQLException{
        User user = userService.findUser(userDetails.getUsername());

        if (user.is2faEnabled()) {
            user.set2faEnabled(false);
            user.setSecretKey(null);
            userService.updateUser(user);
            return "redirect:/dashboard/settings?disabled";
        }

        return "redirect:/setup-2fa";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) throws SQLException {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/dashboard/settings?error=mismatch";
        }

        User user = userService.findUser(userDetails.getUsername());
        userService.updatePassword(user.getUser_id(), newPassword);

        return "redirect:/dashboard/settings?success";
    }
}
