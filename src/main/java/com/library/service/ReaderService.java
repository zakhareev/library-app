package com.library.service;

import com.library.dto.ReaderDTO;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Reader;
import com.library.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Transactional
    public ReaderDTO createReader(ReaderDTO readerDTO) {
        if (readerRepository.existsByEmail(readerDTO.getEmail())) {
            throw new DuplicateResourceException("Reader with email " + readerDTO.getEmail() + " already exists");
        }

        Reader reader = convertToEntity(readerDTO);
        Reader savedReader = readerRepository.save(reader);
        return convertToDTO(savedReader);
    }

    @Transactional
    public ReaderDTO updateReader(Long id, ReaderDTO readerDTO) {
        Reader reader = findReaderById(id);

        reader.setFirstName(readerDTO.getFirstName());
        reader.setLastName(readerDTO.getLastName());
        reader.setEmail(readerDTO.getEmail());
        reader.setPhone(readerDTO.getPhone());
        reader.setAddress(readerDTO.getAddress());

        Reader updatedReader = readerRepository.save(reader);
        return convertToDTO(updatedReader);
    }

    @Transactional
    public void deleteReader(Long id) {
        Reader reader = findReaderById(id);
        readerRepository.delete(reader);
    }

    public ReaderDTO getReaderById(Long id) {
        Reader reader = findReaderById(id);
        return convertToDTO(reader);
    }

    public List<ReaderDTO> getAllReaders() {
        return readerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReaderDTO getReaderByEmail(String email) {
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with email: " + email));
        return convertToDTO(reader);
    }

    private Reader findReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
    }

    private Reader convertToEntity(ReaderDTO dto) {
        Reader reader = new Reader();
        reader.setFirstName(dto.getFirstName());
        reader.setLastName(dto.getLastName());
        reader.setEmail(dto.getEmail());
        reader.setPhone(dto.getPhone());
        reader.setAddress(dto.getAddress());
        return reader;
    }

    private ReaderDTO convertToDTO(Reader reader) {
        ReaderDTO dto = new ReaderDTO();
        dto.setId(reader.getId());
        dto.setFirstName(reader.getFirstName());
        dto.setLastName(reader.getLastName());
        dto.setEmail(reader.getEmail());
        dto.setPhone(reader.getPhone());
        dto.setAddress(reader.getAddress());
        return dto;
    }
}