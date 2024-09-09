package com.inetlibrarymasoe.Repository;

import com.inetlibrarymasoe.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long>
{
    List<Book> findBookByBookTitle(String bookTitle);
    List<Book> findBookByAuthorName(String authorName);
    List<Book> findBookById(Long id);
    Optional<Book> findById(Long id);

    @Query("SELECT b FROM Book b WHERE b.bookStatus LIKE %?1%")
    List<Book> findBookByBookStatus(String bookStatus);

    @Query("SELECT b FROM Book b WHERE b.bookCategory LIKE %?1%")
    List<Book> findBookByBookCategory(String bookCategory);
    Optional<Book> findByIdAndBookStatus(Long bookId, String status);

    @Query("SELECT b FROM Book b WHERE b.bookTitle LIKE %?1% OR b.authorName LIKE %?1% OR str(b.id) LIKE %?1% OR b.bookStatus LIKE %?1% OR b.bookCategory LIKE %?1% OR b.ISBN LIKE %?1%" )
    List<Book> findByKeyword(String keyword);

//    @Query("SELECT b FROM Book b WHERE b.id = ?1")
//    List<Book> findBookById(Long id);

}
