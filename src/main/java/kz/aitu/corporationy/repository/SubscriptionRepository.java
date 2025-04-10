package kz.aitu.corporationy.repository;

import kz.aitu.corporationy.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    List<Subscription> findBySubscriberId(Long subscriberId);

    List<Subscription> findByTargetId(Long targetId);

    Optional<Subscription> findBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    int countByTargetId(Long targetId);
}