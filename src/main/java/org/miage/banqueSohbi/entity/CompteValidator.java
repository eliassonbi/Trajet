package org.miage.banqueSohbi.entity;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

@Service
public class CompteValidator {

  private Validator validator;

  CompteValidator(Validator validator) {
    this.validator = validator;
  }

  public void validate(CompteInput c) {
    Set<ConstraintViolation<CompteInput>> violations = validator.validate(c);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}