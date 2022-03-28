package org.miage.trainSohbi.entity;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

@Service
public class TrajetValidator {

  private Validator validator;

  TrajetValidator(Validator validator) {
    this.validator = validator;
  }

  public void validate(TrajetInput Trajet) {
    Set<ConstraintViolation<TrajetInput>> violations = validator.validate(Trajet);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}