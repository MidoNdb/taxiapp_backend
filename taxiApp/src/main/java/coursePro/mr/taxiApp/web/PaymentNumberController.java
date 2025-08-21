package coursePro.mr.taxiApp.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import coursePro.mr.taxiApp.dto.PaymentNumberDto;
import coursePro.mr.taxiApp.service.PaymentNumberService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/payment-numbers")
public class PaymentNumberController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentNumberController.class);
    private final PaymentNumberService paymentNumberService;
    
    public PaymentNumberController(PaymentNumberService paymentNumberService) {
        this.paymentNumberService = paymentNumberService;
    }
    
    // === ENDPOINTS POUR L'APPLICATION MOBILE (INCHANGÉS) ===
    
    @GetMapping("/conducteur/operateurs")
public ResponseEntity<List<PaymentNumberDto>> getOperateursDisponibles() {
    logger.info("Récupération des opérateurs disponibles");
    List<PaymentNumberDto> operateurs = paymentNumberService.getOperateursDisponibles();
    return ResponseEntity.ok(operateurs);
}
    
    @GetMapping("/conducteur/operateurs/{operateur}/numeros")
    public ResponseEntity<List<PaymentNumberDto>> getNumerosParOperateur(@PathVariable String operateur) {
        logger.info("Récupération des numéros pour l'opérateur: {}", operateur);
        List<PaymentNumberDto> numeros = paymentNumberService.getNumerosParOperateur(operateur);
        
        if (numeros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(numeros);
    }
    
    // === ENDPOINTS POUR L'ADMINISTRATION - CORRECTIONS ===
    
    // ✅ CORRECTION 1: Utiliser getNumeroById au lieu de getNumeroForMobile
    @GetMapping("/admin/{id}")
    public ResponseEntity<PaymentNumberDto> getNumero(@PathVariable Long id) {
        logger.info("Récupération du numéro avec l'ID: {}", id);
        Optional<PaymentNumberDto> numero = paymentNumberService.getNumeroById(id); // ✅ CHANGÉ
        
        if (numero.isPresent()) {
            return ResponseEntity.ok(numero.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/admin/{id}/valider")
    public ResponseEntity<Boolean> validerNumero(@PathVariable Long id) {
        boolean estActif = paymentNumberService.isNumeroActif(id);
        return ResponseEntity.ok(estActif);
    }
    
    // ✅ CORRECTION 2: Ajouter /admin au POST et gérer les erreurs
    @PostMapping("/admin") // ✅ AJOUTÉ /admin
    public ResponseEntity<PaymentNumberDto> creerNumero(@Valid @RequestBody PaymentNumberDto dto) {
        logger.info("Création d'un numéro pour l'opérateur: {}", dto.getOperateur());
        PaymentNumberDto numeroCreé = paymentNumberService.creerNumero(dto);
        
        // ✅ AJOUTÉ: Gérer le cas où le service retourne null
        if (numeroCreé == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(numeroCreé);
    }
    
    // ✅ CORRECTION 3: Gérer les erreurs dans modifierNumero
    @PutMapping("/admin/{id}")
    public ResponseEntity<PaymentNumberDto> modifierNumero(@PathVariable Long id, 
                                                          @Valid @RequestBody PaymentNumberDto dto) {
        logger.info("Modification du numéro avec l'ID: {}", id);
        PaymentNumberDto numeroModifié = paymentNumberService.modifierNumero(id, dto);
        
        // ✅ AJOUTÉ: Gérer le cas où le service retourne null
        if (numeroModifié == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(numeroModifié);
    }
    
    // ✅ CORRECTION 4: Gérer les erreurs dans changerStatut
    @PatchMapping("/admin/{id}/statut")
    public ResponseEntity<PaymentNumberDto> changerStatut(@PathVariable Long id, 
                                                         @RequestParam boolean actif) {
        logger.info("Changement de statut pour l'ID: {} vers: {}", id, actif);
        PaymentNumberDto numeroMisAJour = paymentNumberService.changerStatutNumero(id, actif);
        
        // ✅ AJOUTÉ: Gérer le cas où le service retourne null
        if (numeroMisAJour == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(numeroMisAJour);
    }
    
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> supprimerNumero(@PathVariable Long id) {
        logger.info("Suppression du numéro avec l'ID: {}", id);
        paymentNumberService.supprimerNumero(id);
        return ResponseEntity.noContent().build();
    }
    
    // ✅ CORRECTION 5: Changer readAll en tous
    @GetMapping("/admin/readAll") // ✅ CHANGÉ de readAll vers tous
    public ResponseEntity<List<PaymentNumberDto>> getAllNumeros() {
        List<PaymentNumberDto> numeros = paymentNumberService.getAllNumeros();
        return ResponseEntity.ok(numeros);
    }
    
    @GetMapping("/admin/recherche")
    public ResponseEntity<List<PaymentNumberDto>> rechercherNumeros(
            @RequestParam(required = false) String operateur,
            @RequestParam(required = false) Boolean actif,
            @RequestParam(required = false) String nomProprietaire) {
        
        List<PaymentNumberDto> resultats = paymentNumberService.rechercherNumeros(operateur, actif, nomProprietaire);
        return ResponseEntity.ok(resultats);
    }
    
    @GetMapping("/admin/verifier-existence")
    public ResponseEntity<Boolean> verifierExistence(@RequestParam String numeroTelephone,
                                                     @RequestParam String operateur) {
        boolean existe = paymentNumberService.numeroExiste(numeroTelephone, operateur);
        return ResponseEntity.ok(existe);
    }
}