package coursePro.mr.taxiApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import coursePro.mr.taxiApp.entity.PaymentNumber;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface PaymentNumberRepository extends JpaRepository<PaymentNumber, Long> {
    
    // Trouver tous les numéros actifs
    List<PaymentNumber> findByActifTrue();
    
    // Trouver les numéros par opérateur
    List<PaymentNumber> findByOperateurAndActifTrue(String operateur);
    
    // Trouver tous les numéros d'un opérateur (actifs et inactifs)
    List<PaymentNumber> findByOperateur(String operateur);
    
    // Vérifier si un numéro existe déjà
    boolean existsByNumeroTelephoneAndOperateur(String numeroTelephone, String operateur);
    
    // Trouver un numéro spécifique actif
    Optional<PaymentNumber> findByIdAndActifTrue(Long id);
    
    // Trouver par numéro de téléphone
    Optional<PaymentNumber> findByNumeroTelephone(String numeroTelephone);
    
    // Requête pour obtenir la liste des opérateurs distincts avec leurs nombres de comptes
    @Query("SELECT DISTINCT p.operateur, COUNT(p) as nombreComptes, " +
           "SUM(CASE WHEN p.actif = true THEN 1 ELSE 0 END) as comptesActifs " +
           "FROM PaymentNumber p " +
           "GROUP BY p.operateur " +
           "ORDER BY p.operateur")
    List<Object[]> findOperateursWithCounts();
    
    // Requête pour obtenir les opérateurs qui ont au moins un compte actif
    @Query("SELECT DISTINCT p.operateur FROM PaymentNumber p WHERE p.actif = true ORDER BY p.operateur")
    List<String> findOperateursActifs();
    
    // Compter les numéros actifs par opérateur
    @Query("SELECT COUNT(p) FROM PaymentNumber p WHERE p.operateur = :operateur AND p.actif = true")
    Long countByOperateurAndActifTrue(@Param("operateur") String operateur);
    
    // Trouver le premier numéro actif d'un opérateur (pour sélection par défaut)
    Optional<PaymentNumber> findFirstByOperateurAndActifTrueOrderByDateCreationAsc(String operateur);
    
    // Requête pour obtenir les statistiques par opérateur
    @Query("SELECT p.operateur, " +
           "COUNT(p) as total, " +
           "SUM(CASE WHEN p.actif = true THEN 1 ELSE 0 END) as actifs, " +
           "MIN(p.dateCreation) as premierAjout, " +
           "MAX(p.dateModification) as derniereModification " +
           "FROM PaymentNumber p " +
           "GROUP BY p.operateur")
    List<Object[]> getStatistiquesParOperateur();
    
    // Recherche par nom de propriétaire (pour l'admin)
    List<PaymentNumber> findByNomProprietaireContainingIgnoreCase(String nomProprietaire);
    
    // Trouver les numéros récemment modifiés
    @Query("SELECT p FROM PaymentNumber p ORDER BY p.dateModification DESC")
    List<PaymentNumber> findRecentlyModified();
}
