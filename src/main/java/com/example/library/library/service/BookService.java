package com.example.library.library.service;

import com.example.library.library.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book createNewBook(Book user, MultipartFile maintenanceFile);

    Book updateBook(Book user);

    Book getBookById(Long pid);

    void deleteBookById(Long pid);

    List<Book> filterBook(String filterText);
}
