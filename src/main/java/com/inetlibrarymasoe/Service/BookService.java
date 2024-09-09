package com.inetlibrarymasoe.Service;


import com.inetlibrarymasoe.Entity.Book;
import com.inetlibrarymasoe.Exception.BookNotFoundException;
import com.inetlibrarymasoe.Exception.BookOnLoanException;
import com.inetlibrarymasoe.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepo;

    public List<Book> ListAll(){
        return (List<Book>) bookRepo.findAll();
    }

    public void save(Book book){
        bookRepo.save(book);
    }

    public Book get(Long id) throws BookNotFoundException {
        Optional<Book> result=bookRepo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw  new BookNotFoundException("Could not find any Book.");
    }

    public void delete(Long id) throws BookNotFoundException, BookOnLoanException {
        Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Could not find any book with ID " + id));

        if (book.getBookStatus().equals("On loan")) {
            throw new BookOnLoanException("The book with ID " + id + " is currently on loan and cannot be deleted.");
        }

        bookRepo.deleteById(id);
    }


    public List<Book> findByBookTitle(String bookTitle){
        return  (List<Book>) bookRepo.findBookByBookTitle(bookTitle);
    }
    public List<Book> findAuthorName(String authorName){
        return (List<Book>) bookRepo.findBookByAuthorName(authorName);
    }
    public List<Book> findByBookStatus(String bookStatus){
        return (List<Book>) bookRepo.findBookByBookStatus(bookStatus);
    }

    public List<Book> findBookByBookCategory(String bookCategory){
        return (List<Book>) bookRepo.findBookByBookCategory(bookCategory);
    }

    public Optional<Book> findByBookid1(Long bid) {
        return bookRepo.findById(bid);
    }

    public List<Book> findByBookid(Long id){
        return (List<Book>) bookRepo.findBookById(id);
    }

    public List<Book> findByKeyword(String keyword) {
        return bookRepo.findByKeyword(keyword);
    }
}
