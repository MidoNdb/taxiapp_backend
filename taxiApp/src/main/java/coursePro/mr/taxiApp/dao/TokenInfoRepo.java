package coursePro.mr.taxiApp.dao;


import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coursePro.mr.taxiApp.entity.TokenInfo;

@Repository
public interface TokenInfoRepo extends JpaRepository<TokenInfo, Long> {

    // Trouver un token par son refreshToken
    Optional<TokenInfo> findByRefreshToken(String refreshToken);

    // Liste tous les tokens appartenant à un utilisateur
    List<TokenInfo> findByUserId(Long userId);
}

