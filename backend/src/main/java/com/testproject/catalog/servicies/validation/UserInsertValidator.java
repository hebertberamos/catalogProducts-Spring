package com.testproject.catalog.servicies.validation;

import java.util.ArrayList;
import java.util.List;

import com.testproject.catalog.dtos.UserInsertDTO;
import com.testproject.catalog.entities.User;
import com.testproject.catalog.repositories.UserRepository;
import com.testproject.catalog.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        // Testar para ver se o email já existe dentro do banco de dados.
        User user = repository.findByEmail(dto.getEmail());
        if(user != null){
            list.add(new FieldMessage("Email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
