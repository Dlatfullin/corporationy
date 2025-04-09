package kz.aitu.corporationy.repository;

import kz.aitu.corporationy.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}