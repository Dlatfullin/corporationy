package kz.aitu.corporationy.repository;

import kz.aitu.corporationy.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByHashAndExpiredAtAfter(byte[] hash, LocalDateTime expiredAt);
}