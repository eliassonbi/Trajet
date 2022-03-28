package org.miage.banqueSohbi.assembler ;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.lang.Object ;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.miage.banqueSohbi.boundary.CompteRepresentation;
import org.miage.banqueSohbi.entity.Compte;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CompteAssembler implements RepresentationModelAssembler<Compte, EntityModel<Compte>> {

  @Override
  public EntityModel<Compte> toModel(Compte Compte) {
    return EntityModel.of(Compte, 
                   
							     linkTo(methodOn(CompteRepresentation.class)
							            .getAllComptes()).withRel("collection")
                   )
                          
                          ;
  }

  public CollectionModel<EntityModel<Compte>> toCollectionModel(Iterable<? extends Compte> entities) {
      List<EntityModel<Compte>> CompteModel = StreamSupport
        				.stream(entities.spliterator(), false)
        				.map(i -> toModel(i))
        				.collect(Collectors.toList());
      return CollectionModel.of(CompteModel,                                					
              linkTo(methodOn(CompteRepresentation.class)
               			.getAllComptes()).withSelfRel());
  }

 
}