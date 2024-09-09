package com.inetlibrarymasoe.Controller;

import com.inetlibrarymasoe.Entity.Book;
import com.inetlibrarymasoe.Entity.Loan;
import com.inetlibrarymasoe.Entity.User;
import com.inetlibrarymasoe.Exception.BookNotFoundException;
import com.inetlibrarymasoe.Exception.BookOnLoanException;
import com.inetlibrarymasoe.Exception.MaxBooksBorrowedException;
import com.inetlibrarymasoe.Service.BookService;
import com.inetlibrarymasoe.Service.LoanService;
import com.inetlibrarymasoe.Service.RoleService;
import com.inetlibrarymasoe.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BookService bookService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/adminIndex")
    public String showMain(Model model,@RequestParam(name="searchType",required = false) String searchType,
                           @RequestParam(name = "keyword",required = false)String keyword){
        List<Book> listBook;
        if (keyword != null) {
            listBook = bookService.findByKeyword(keyword);
        } else {
            listBook = bookService.ListAll();
        }
        model.addAttribute("ListBooks", listBook);
        return "adminIndex";
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("rolelist",roleService.findAll());
        return "login";
    }

//    @GetMapping("/regis")
//    public String regForm(Model model){
//        model.addAttribute("user",new User());
//        model.addAttribute("rolelist",roleService.findAll());
//        return "register";
//    }

    @PostMapping("/regis")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        user.setVerifyStatus(null);
        userService.saveUser(user);
        return "redirect:/admin/login";
    }
    @PostMapping("/login")
    public String showLoginForm(@RequestParam(name = "username") String email,
                                @RequestParam(name = "password") String password,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/adminIndex";
            } else if (user.getRole().getName().equalsIgnoreCase("USER") && user.getVerifyStatus() != null) {
                return "redirect:/user/user-page/" + user.getId();
            } else {
                return "redirect:/admin/waitPlease";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/admin/login";
    }

    @GetMapping("/addBook")
    public String showAddBook(Model model){
        model.addAttribute("book",new Book());
        return "addBook";
    }

    @PostMapping("/book/save")
    public String saveBook(Book book) {
        bookService.save(book);
        return "redirect:/admin/adminIndex";
    }

    @GetMapping("/book/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            Book book = bookService.get(id);
            model.addAttribute("book", book);
            return "editBook";
        } catch (BookNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/adminIndex";
        }
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            bookService.delete(id);
        } catch (BookNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        } catch (BookOnLoanException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/adminIndex";
    }

    @GetMapping("/addNewUser")
    public String addUser(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("rolelist", roleService.findAll());
        return "addNewUser";
    }
    @PostMapping("/user/save")
    public String saveUser(User user) {
        user.setVerifyStatus("Verified");
        userService.saveUser(user);
        return "redirect:/admin/adminIndex/userManagement";
    }

    @GetMapping("/verifyPage")
    public String showNextPage() {
        return "verifyUser";
    }

    @GetMapping("/adminIndex/userManagement")
    public String userManageForm(Model model,
                                 @RequestParam(name = "keyword", required = false) String keyword) {
        List<User> listUsers;
        if (keyword != null && !keyword.isEmpty()) {
            listUsers = userService.findByKeyword(keyword);
        } else {
            listUsers = userService.findAll();
        }
        model.addAttribute("Users", listUsers);
        return "userManagePage";
    }


    @GetMapping("/verify")
    public String showVerify(Model model,
                             @RequestParam(name = "keyword", required = false) String keyword) {
        List<User> listUsers;
        if (keyword != null && !keyword.isEmpty()) {
            listUsers = userService.findByKeyword(keyword);
        } else {
            listUsers = userService.getUnverifiedUsers();
        }
        model.addAttribute("unverifyusers", listUsers);
        return "verifyUser";
    }



    @GetMapping("/verify/yes/{id}")
    public String verifyUser(@PathVariable("id") Long id, Model model) {
        userService.verifyMemberProcess(id);
        return "redirect:/admin/adminIndex/userManagement";
    }



    @GetMapping("/adminIndex/new")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "AddNewBooks";
    }

    @GetMapping("/borrowBook")
    public String bookBorrow(Model model,
                             @RequestParam(name = "keyword", required = false) String keyword,
                             @RequestParam(name = "id", required = false) Long id) {
        List<Book> listBook;
        if (id != null) {
            listBook = bookService.findByBookid(id);
        }else if (keyword != null && !keyword.isEmpty()) {
            listBook = bookService.findByKeyword(keyword);
        }
        else {
            listBook = bookService.findByBookStatus("Available");
        }
        model.addAttribute("books", listBook);
        return "borrowBook";
    }

    @GetMapping("/borrowBook/borrow/{id}")
    public String borrowBook(Model model, @PathVariable("id") Long bookId, RedirectAttributes ra) {
        try {
            Book book = bookService.get(bookId);
            Loan loan = new Loan();
            loan.setBook(book);
            model.addAttribute("loan", loan);
            return "addLoan";
        } catch (BookNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/borrowBook";
        }
    }
    @PostMapping("/loan/save")
    public String saveLoan(@ModelAttribute Loan loan, RedirectAttributes ra) {
        try {
            loanService.borrowBook(loan.getUser().getId(), loan.getBook().getId());
            return "redirect:/admin/borrowBook";
        } catch (MaxBooksBorrowedException e) {
            ra.addFlashAttribute("warning", e.getMessage());
            return "redirect:/admin/borrowBook";
        } catch (Exception e) {
            ra.addFlashAttribute("message", "An error occurred while borrowing the book.");
            return "redirect:/admin/borrowBook";
        }
    }
    @GetMapping("/returnBook")
    public String showRetrun(Model model){
        List<Loan> loanList = loanService.ListAll();
        model.addAttribute("loans",loanList);
        return "returnBook";
    }

    @PostMapping("/returnBook/return/{id}")
    public String returnBook(@PathVariable Long id, Model model){
        loanService.returnBook(id);
        return "redirect:/admin/returnBook";
    }

    @GetMapping("/test")
    public String test(){
        return "userIndex";
    }

    @GetMapping("/waitPlease")
    public String showWait(){
        return "wait";
    }

    
}
