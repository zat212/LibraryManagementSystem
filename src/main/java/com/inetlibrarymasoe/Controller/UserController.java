package com.inetlibrarymasoe.Controller;



import com.inetlibrarymasoe.Entity.Book;
import com.inetlibrarymasoe.Entity.Loan;
import com.inetlibrarymasoe.Entity.User;
import com.inetlibrarymasoe.Service.BookService;
import com.inetlibrarymasoe.Service.LoanService;
import com.inetlibrarymasoe.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/book-detail/{bid}/{id}")
    public String getBookDetail(@PathVariable("bid") Long bid, @PathVariable("id") long id, Model model) {
        Book book = bookService.findByBookid1(bid).get();
        model.addAttribute("user", userService.findById(id).orElse(null));
        model.addAttribute("id", id);
        model.addAttribute("book", book);
        return "userBookDetail";
    }
    @GetMapping("/checkTable/{id}")
    public String showList(Model model, @PathVariable long id) {
        List<Loan> loans = loanService.findLoansByUserId(id);
        model.addAttribute("loans", loans);
        model.addAttribute("id", id);
        model.addAttribute("user", userService.findById(id).get());
        model.addAttribute("userPageUrl", "/user/user-page/" + id); // Add this line
        return "checkBorrowList";
    }
    @GetMapping("/editPro/{id}")
    public String showProfile(Model model, @PathVariable long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("id", id);
            return "editProfile";
        } else {
            // Handle user not found case
            return "redirect:/user/notfound"; // Or any appropriate error handling
        }
    }

    @GetMapping("/user-page/{id}")
    public String userPage(Model model, @PathVariable Long id) {
        if (id == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("listBook", bookService.ListAll());
        model.addAttribute("user", userService.findById(id).orElse(null));
        model.addAttribute("id", id);
        return "index";
    }
    @PostMapping("/editPro/{id}")
    public String updateProfile(@PathVariable long id, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user);
            long updatedUserId = user.getId();
            redirectAttributes.addAttribute("id", updatedUserId);
            return "redirect:/user/user-page/{id}";
        } catch (Exception e) {
            e.printStackTrace();
            return "editProfile";
        }
    }
}
