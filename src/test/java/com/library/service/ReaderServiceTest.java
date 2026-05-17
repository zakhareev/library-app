package com.library.service;

import com.library.dto.ReaderDTO;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Reader;
import com.library.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private ReaderService readerService;

    private Reader reader;
    private ReaderDTO readerDTO;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.setId(1L);
        reader.setFirstName("John");
        reader.setLastName("Doe");
        reader.setEmail("john.doe@example.com");
        reader.setPhone("1234567890");
        reader.setAddress("123 Main St");

        readerDTO = new ReaderDTO();
        readerDTO.setFirstName("John");
        readerDTO.setLastName("Doe");
        readerDTO.setEmail("john.doe@example.com");
        readerDTO.setPhone("1234567890");
        readerDTO.setAddress("123 Main St");
    }

    @Test
    void createReader_Success() {
        when(readerRepository.existsByEmail(readerDTO.getEmail())).thenReturn(false);
        when(readerRepository.save(any(Reader.class))).thenReturn(reader);

        ReaderDTO result = readerService.createReader(readerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(readerDTO.getEmail());
        verify(readerRepository, times(1)).existsByEmail(readerDTO.getEmail());
        verify(readerRepository, times(1)).save(any(Reader.class));
    }

    @Test
    void createReader_DuplicateEmail_ThrowsException() {
        when(readerRepository.existsByEmail(readerDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> readerService.createReader(readerDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
        verify(readerRepository, never()).save(any(Reader.class));
    }

    @Test
    void getReaderById_Success() {
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        ReaderDTO result = readerService.getReaderById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(readerRepository, times(1)).findById(1L);
    }

    @Test
    void getReaderById_NotFound_ThrowsException() {
        when(readerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readerService.getReaderById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reader not found with id: 999");
    }

    @Test
    void updateReader_Success() {
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(readerRepository.save(any(Reader.class))).thenReturn(reader);

        readerDTO.setFirstName("Jane");
        ReaderDTO result = readerService.updateReader(1L, readerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(readerRepository, times(1)).findById(1L);
        verify(readerRepository, times(1)).save(any(Reader.class));
    }

    @Test
    void deleteReader_Success() {
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        doNothing().when(readerRepository).delete(reader);

        readerService.deleteReader(1L);

        verify(readerRepository, times(1)).findById(1L);
        verify(readerRepository, times(1)).delete(reader);
    }

    @Test
    void getAllReaders_Success() {
        when(readerRepository.findAll()).thenReturn(Arrays.asList(reader));

        List<ReaderDTO> result = readerService.getAllReaders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
        verify(readerRepository, times(1)).findAll();
    }

    @Test
    void getReaderByEmail_Success() {
        when(readerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(reader));

        ReaderDTO result = readerService.getReaderByEmail("john.doe@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(readerRepository, times(1)).findByEmail("john.doe@example.com");
    }
}