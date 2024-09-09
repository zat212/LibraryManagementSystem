package com.inetlibrarymasoe.Repository;


import com.inetlibrarymasoe.Entity.Loan;
import com.inetlibrarymasoe.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByUserAndReturnDateIsNull(User user);

    List<Loan> findByUserId(long userId);
}
