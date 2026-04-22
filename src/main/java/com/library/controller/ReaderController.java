package com.library.controller;

import com.library.dto.ReaderDTO;
import com.library.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @PostMapping
    public ResponseEntity<ReaderDTO> createReader(@Valid @RequestBody ReaderDTO readerDTO) {
        ReaderDTO createdReader = readerService.createReader(readerDTO);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderDTO> updateReader(@PathVariable Long id, @Valid @RequestBody ReaderDTO readerDTO) {
        ReaderDTO updatedReader = readerService.updateReader(id, readerDTO);
        return ResponseEntity.ok(updatedReader);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDTO> getReaderById(@PathVariable Long id) {
        ReaderDTO reader = readerService.getReaderById(id);
        return ResponseEntity.ok(reader);
    }

    @GetMapping
    public ResponseEntity<List<ReaderDTO>> getAllReaders() {
        List<ReaderDTO> readers = readerService.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ReaderDTO> getReaderByEmail(@PathVariable String email) {
        ReaderDTO reader = readerService.getReaderByEmail(email);
        return ResponseEntity.ok(reader);
    }
}