package coursePro.mr.taxiApp.service;



import java.util.List;
import java.util.Optional;

import coursePro.mr.taxiApp.dto.PaymentNumberDto;

public interface PaymentNumberService {
    
    // === MÉTHODES POUR L'API MOBILE ===
    
    /**
     * Récupère la liste des opérateurs disponibles avec au moins un numéro actif
     * @return Liste des opérateurs disponibles
     */
    List<PaymentNumberDto> getOperateursDisponibles();
    
    /**
     * Récupère les numéros de paiement actifs pour un opérateur donné
     * @param operateur Le nom de l'opérateur
     * @return Liste des numéros actifs pour cet opérateur
     */
    List<PaymentNumberDto> getNumerosParOperateur(String operateur);
    
    /**
     * Récupère un numéro de paiement spécifique par son ID (version mobile enrichie)
     * @param id L'ID du numéro
     * @return Le numéro avec informations d'affichage
     */
    Optional<PaymentNumberDto> getNumeroForMobile(Long id);
    
    /**
     * Valide qu'un numéro est toujours actif et disponible pour le paiement
     * @param id L'ID du numéro à valider
     * @return true si le numéro est valide et actif
     */
    boolean isNumeroActif(Long id);
    
    // === MÉTHODES POUR L'ADMINISTRATION ===
    
    /**
     * Crée un nouveau numéro de paiement
     * @param dto Les données du numéro à créer
     * @return Le numéro créé
     */
    PaymentNumberDto creerNumero(PaymentNumberDto dto);
    
    /**
     * Met à jour un numéro existant
     * @param id L'ID du numéro à modifier
     * @param dto Les nouvelles données
     * @return Le numéro mis à jour
     */
    PaymentNumberDto modifierNumero(Long id, PaymentNumberDto dto);
    
    /**
     * Active ou désactive un numéro
     * @param id L'ID du numéro
     * @param actif true pour activer, false pour désactiver
     * @return Le numéro mis à jour
     */
    PaymentNumberDto changerStatutNumero(Long id, boolean actif);
    
    /**
     * Supprime définitivement un numéro
     * @param id L'ID du numéro à supprimer
     */
    void supprimerNumero(Long id);
    
    /**
     * Récupère tous les numéros (pour l'admin)
     * @return Liste complète des numéros
     */
    List<PaymentNumberDto> getAllNumeros();
    
    /**
     * Récupère un numéro par son ID (version admin complète)
     * @param id L'ID du numéro
     * @return Le numéro complet
     */
    Optional<PaymentNumberDto> getNumeroById(Long id);
    
    /**
     * Recherche des numéros par critères
     * @param operateur Filtrer par opérateur (optionnel)
     * @param actif Filtrer par statut (optionnel)
     * @param nomProprietaire Rechercher dans le nom du propriétaire (optionnel)
     * @return Liste filtrée des numéros
     */
    List<PaymentNumberDto> rechercherNumeros(String operateur, Boolean actif, String nomProprietaire);
    
    /**
     * Vérifie si un numéro existe déjà pour un opérateur donné
     * @param numeroTelephone Le numéro à vérifier
     * @param operateur L'opérateur
     * @return true si le numéro existe déjà
     */
    boolean numeroExiste(String numeroTelephone, String operateur);
    
    /**
     * Compte le nombre de numéros actifs par opérateur
     * @param operateur L'opérateur
     * @return Nombre de numéros actifs
     */
    long countNumerosActifsParOperateur(String operateur);
}
