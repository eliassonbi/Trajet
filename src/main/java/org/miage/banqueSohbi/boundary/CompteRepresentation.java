package org.miage.banqueSohbi.boundary;
import org.miage.banqueSohbi.assembler.CompteAssembler;
import org.miage.banqueSohbi.entity.Compte;
import org.miage.banqueSohbi.entity.CompteValidator;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value="/Banque", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Compte.class)
public class CompteRepresentation {

    private final CompteResource ir;
    private final CompteAssembler assembler;
    private final CompteValidator validator;

    public CompteRepresentation(CompteResource ir, 
                                    CompteAssembler assembler,
                                    CompteValidator validator) {
        this.ir = ir;
        this.assembler = assembler;
        this.validator = validator;
    }

    // GET all
    @GetMapping
    public ResponseEntity<?> getAllComptes() {
        return ResponseEntity.ok(assembler.toCollectionModel(ir.findAll()));
    }

     // GET one
     @GetMapping(value="/{userId}")
     public ResponseEntity<?> getOneCompte(@PathVariable("userId") String id) {
         return Optional.ofNullable(ir.findById(id)).filter(Optional::isPresent)
                 .map(i -> ResponseEntity.ok(assembler.toModel(i.get())))
                 .orElse(ResponseEntity.notFound().build());
     }
     
    @GetMapping(value="/{userId}/{prix}")
    public ResponseEntity<Boolean> payement(@PathVariable("userId") String id, @PathVariable("prix") double prix) {

        Compte c= ir.findById(id).get();
        if(c.getSolde()-prix<0){
            return new ResponseEntity<Boolean>(false,HttpStatus.OK);
        } 
        c.setSolde(c.getSolde()-prix);
        ir.save(c);

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
        
    }














}
