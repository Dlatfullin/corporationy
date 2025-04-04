package kz.aitu.corporationy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] hash;
    private LocalDateTime expiredAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Token(byte[] hash, LocalDateTime expiredAt, User user) {
        this.hash = hash;
        this.expiredAt = expiredAt;
        this.user = user;
    }
}