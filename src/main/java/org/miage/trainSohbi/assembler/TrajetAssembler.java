package org.miage.trainSohbi.assembler ;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.lang.Object ;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.miage.trainSohbi.entity.Trajet;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.miage.trainSohbi.boundary.TrajetRepresentation;

@Component
public class TrajetAssembler implements RepresentationModelAssembler<Trajet, EntityModel<Trajet>> {

  @Override
  public EntityModel<Trajet> toModel(Trajet Trajet) {
    return EntityModel.of(Trajet, 
                   linkTo(methodOn(TrajetRepresentation.class)
                          .getOneTrajet(Trajet.getId())).withSelfRel(),
							     linkTo(methodOn(TrajetRepresentation.class)
							            .getAllTrajets()).withRel("collection"),
                   linkTo(methodOn(TrajetRepresentation.class)
							            .getRetour(Trajet.getId())).withRel("retour"))
                          
                          ;
  }

  public CollectionModel<EntityModel<Trajet>> toCollectionModel(Iterable<? extends Trajet> entities) {
      List<EntityModel<Trajet>> TrajetModel = StreamSupport
        				.stream(entities.spliterator(), false)
        				.map(i -> toModel(i))
        				.collect(Collectors.toList());
      return CollectionModel.of(TrajetModel,                                					
              linkTo(methodOn(TrajetRepresentation.class)
               			.getAllTrajets()).withSelfRel());
  }

  public CollectionModel<EntityModel<Trajet>> ConcatCollectionModel(Iterable<? extends Trajet> entities, Iterable<? extends Trajet> entities2) {
    List<EntityModel<Trajet>> TrajetModel = StreamSupport
              .stream(( Stream.concat(StreamSupport.stream(entities.spliterator(), false), 
              StreamSupport.stream(entities2.spliterator(), false))).spliterator(), false)
              .map(i -> toModel(i))
              .collect(Collectors.toList());
    return CollectionModel.of(TrajetModel,                                					
            linkTo(methodOn(TrajetRepresentation.class)
                   .getAllTrajets()).withSelfRel());
}
}