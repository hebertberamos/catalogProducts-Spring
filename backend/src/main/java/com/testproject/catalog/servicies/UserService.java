package com.testproject.catalog.servicies;

import com.testproject.catalog.dtos.UserDTO;
import com.testproject.catalog.dtos.UserUpdateDTO;
import com.testproject.catalog.entities.User;
import com.testproject.catalog.repositories.UserRepository;
import com.testproject.catalog.servicies.exceptions.DatabaseException;
import com.testproject.catalog.servicies.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;


    @Transactional
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User prod = obj.orElseThrow(() -> new ResourcesNotFoundException("User no found"));

        return new UserDTO(prod);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User user = repository.findById(id).get();

            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setRole(dto.getRole());

            user = repository.save(user);
            return new UserDTO(user);
        }
        catch(EntityNotFoundException e) {
            throw new ResourcesNotFoundException("Id not found");
        }
    }

    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourcesNotFoundException("Id not found (" + id + ")");
        }
        catch(DataIntegrityViolationException e) {
            throw new DatabaseException("This category can not be deleted. LINKED PRODUTCS");
        }
    }

}
