package com.inetlibrarymasoe.Service;




import com.inetlibrarymasoe.Entity.Book;
import com.inetlibrarymasoe.Entity.Loan;
import com.inetlibrarymasoe.Entity.User;
import com.inetlibrarymasoe.Exception.MaxBooksBorrowedException;
import com.inetlibrarymasoe.Repository.BookRepository;
import com.inetlibrarymasoe.Repository.LoanRepository;
import com.inetlibrarymasoe.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public void borrowBook(Long userId, Long bookId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Optional<Book> optionalBook = bookRepository.findByIdAndBookStatus(bookId, "Available");

        Book book = optionalBook.get();

        List<Loan> currentLoans = loanRepository.findByUserAndReturnDateIsNull(user);
        if (currentLoans.size() == 4) {
            throw new MaxBooksBorrowedException("Cannot Borrow More than 3 Books");
        }

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date dueDate = calendar.getTime();
        Loan newLoan = new Loan(user, book, today, dueDate);
        book.setBookStatus("On Loan");
        bookRepository.save(book);
        loanRepository.save(newLoan);
    }

    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));
        Date today = new Date();
        loan.setReturnDate(today);
        Book book = loan.getBook();
        book.setBookStatus("Available");
        bookRepository.save(book);
        loanRepository.delete(loan);

    }

    public List<Loan> ListAll(){
        return loanRepository.findAll();
    }
    public List<Loan> findLoansByUserId(long userId) {
        return loanRepository.findByUserId(userId);
    }
}
