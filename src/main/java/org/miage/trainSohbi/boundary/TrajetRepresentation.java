package org.miage.trainSohbi.boundary;
import org.springframework.util.ReflectionUtils;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.lang.reflect.Field;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.miage.trainSohbi.assembler.TrajetAssembler;
import org.miage.trainSohbi.entity.Trajet;
import org.miage.trainSohbi.entity.TrajetInput;
import org.miage.trainSohbi.entity.TrajetValidator;

@RestController
@RequestMapping(value="/Trajets", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Trajet.class)
public class TrajetRepresentation {

    private final TrajetResource ir;
    private final TrajetAssembler assembler;
    private final TrajetValidator validator;

    public TrajetRepresentation(TrajetResource ir, 
                                    TrajetAssembler assembler,
                                    TrajetValidator validator) {
        this.ir = ir;
        this.assembler = assembler;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<?> getAllTrajets() {
        return ResponseEntity.ok(assembler.toCollectionModel(ir.findAll()));
    }

    @GetMapping(value="/{TrajetId}")
    public ResponseEntity<?> getOneTrajet(@PathVariable("TrajetId") String id) {
        return Optional.ofNullable(ir.findById(id)).filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(assembler.toModel(i.get())))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET retour
    @GetMapping(value="/{TrajetID}/retour")
    public ResponseEntity<?> getRetour(@PathVariable("TrajetID") String id){
        Trajet t = ir.findById(id).get();
       
                       
        return 
        
        ResponseEntity.ok(assembler.toCollectionModel(
         ir.findByLieudepartAndLieuarrivee(t.getLieuarrivee(),
         t.getLieudepart()).stream().filter(i -> i.getDatedepart().compareTo(t.getDatearrivee()) > 0
         && !i.getReserve())
         .collect(Collectors.toList())
         )); 
    }

    @PostMapping("/Trajets/post") 
    public ResponseEntity<?> postAlleSimple(@RequestBody @Valid TrajetInput Trajet ){
        
        
        Calendar calendar = Calendar.getInstance();
      
        calendar.setTime(dateFormat(Trajet.getDatedepart()));
    
        calendar.add(Calendar.HOUR_OF_DAY, 1);

         return ResponseEntity.ok(assembler.toCollectionModel(
                ir.findByLieudepartAndLieuarriveeAndCote(Trajet.getLieudepart(), Trajet.getLieuarrivee(), Trajet.getCote())
                .stream().filter(i-> (dateFormat(i.getDatedepart()).after(dateFormat(Trajet.getDatedepart())) && dateFormat(i.getDatedepart()).before(calendar.getTime())) )
                .collect(Collectors.toList()))); 
            
    }
    private Date dateFormat (String date){
        SimpleDateFormat dateSF = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return dateSF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> saveTrajet(@RequestBody @Valid TrajetInput Trajet)  {
        Trajet Trajet2Save = new Trajet(
            UUID.randomUUID().toString(),
            Trajet.getLieudepart(),
            Trajet.getLieuarrivee(),
            Trajet.getDatedepart(),
            Trajet.getDatearrivee(),
            Trajet.getPrix(),
            Trajet.getCote(),
            Trajet.getReserve()
        );
        Trajet saved = ir.save(Trajet2Save);
        URI location = linkTo(TrajetRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    // DELETE
    @DeleteMapping(value = "/{TrajetId}")
    @Transactional
    public ResponseEntity<?> deleteTrajet(@PathVariable("TrajetId") String TrajetId) {
        Optional<Trajet> Trajet = ir.findById(TrajetId);
        if (Trajet.isPresent()) {
            ir.delete(Trajet.get());
        }
        return ResponseEntity.noContent().build();
    }

   
    @GetMapping(value = "/Reserver/{TrajetId}/{userId}")
    @Transactional
    public ResponseEntity<?> ReserveTrajet(
            @PathVariable("TrajetId") String TrajetId,
            @PathVariable("userId") String userId) {
        Optional<Trajet> t= ir.findById(TrajetId);

        if (!t.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        if (!ir.existsById(TrajetId)) {
            return ResponseEntity.notFound().build();
        }
    
        if(t.get().getReserve().equals(false)){
            final String uri = "http://localhost:8082/Banque/{userId}/{prix}";

            RestTemplate restTemplate = new RestTemplate();
            boolean result = restTemplate.getForObject(uri, boolean.class, userId, t.get().getPrix());
            if(result){
                t.get().setReserve(true);
                ir.save(t.get());
                return ResponseEntity.ok().build();
             }
             
            
        }
        
        return ResponseEntity.notFound().build();
    }


    // PATCH
    @PatchMapping(value = "/{TrajetId}")
    @Transactional
    public ResponseEntity<?> updateTrajetPartiel(@PathVariable("TrajetId") String TrajetId,
            @RequestBody Map<Object, Object> fields) {
        Optional<Trajet> body = ir.findById(TrajetId);
        if (body.isPresent()) {
            Trajet Trajet = body.get();
            fields.forEach((f, v) -> {
                Field field = ReflectionUtils.findField(Trajet.class, f.toString());
                field.setAccessible(true);
                ReflectionUtils.setField(field, Trajet, v);
            });
            validator.validate(new TrajetInput(Trajet.getDatedepart(), Trajet.getDatearrivee(), 
            Trajet.getLieudepart(),Trajet.getLieuarrivee(), Trajet.getPrix(), Trajet.getCote(), Trajet.getReserve()));
            Trajet.setId(TrajetId);
            ir.save(Trajet);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }















}
