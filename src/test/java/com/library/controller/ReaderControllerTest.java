package com.library.controller;

import com.library.dto.ReaderDTO;
import com.library.service.ReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderControllerTest {

    @Mock
    private ReaderService readerService;

    @InjectMocks
    private ReaderController readerController;

    private ReaderDTO readerDTO;

    @BeforeEach
    void setUp() {
        readerDTO = new ReaderDTO();
        readerDTO.setId(1L);
        readerDTO.setFirstName("John");
        readerDTO.setLastName("Doe");
        readerDTO.setEmail("john@example.com");
        readerDTO.setPhone("1234567890");
        readerDTO.setAddress("123 Main St");
    }

    @Test
    void createReader_ShouldReturnCreated() {
        when(readerService.createReader(any(ReaderDTO.class))).thenReturn(readerDTO);

        ResponseEntity<ReaderDTO> response = readerController.createReader(readerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
        verify(readerService, times(1)).createReader(any(ReaderDTO.class));
    }

    @Test
    void getAllReaders_ShouldReturnOk() {
        when(readerService.getAllReaders()).thenReturn(Arrays.asList(readerDTO));

        ResponseEntity<List<ReaderDTO>> response = readerController.getAllReaders();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(readerService, times(1)).getAllReaders();
    }

    @Test
    void getReaderById_ShouldReturnOk() {
        when(readerService.getReaderById(1L)).thenReturn(readerDTO);

        ResponseEntity<ReaderDTO> response = readerController.getReaderById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(readerService, times(1)).getReaderById(1L);
    }

    @Test
    void updateReader_ShouldReturnOk() {
        when(readerService.updateReader(eq(1L), any(ReaderDTO.class))).thenReturn(readerDTO);

        ResponseEntity<ReaderDTO> response = readerController.updateReader(1L, readerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(readerService, times(1)).updateReader(eq(1L), any(ReaderDTO.class));
    }

    @Test
    void deleteReader_ShouldReturnNoContent() {
        doNothing().when(readerService).deleteReader(1L);

        ResponseEntity<Void> response = readerController.deleteReader(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(readerService, times(1)).deleteReader(1L);
    }

    @Test
    void getReaderByEmail_ShouldReturnOk() {
        when(readerService.getReaderByEmail("john@example.com")).thenReturn(readerDTO);

        ResponseEntity<ReaderDTO> response = readerController.getReaderByEmail("john@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
        verify(readerService, times(1)).getReaderByEmail("john@example.com");
    }
}