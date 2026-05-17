package com.library.service;

import com.library.dto.BookDTO;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = findBookById(id);

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookById(id);
        bookRepository.delete(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = findBookById(id);
        return convertToDTO(book);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByAvailableTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        return book;
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAvailable(book.getAvailable());
        return dto;
    }
}