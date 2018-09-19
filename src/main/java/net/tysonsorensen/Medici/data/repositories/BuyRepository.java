package net.tysonsorensen.Medici.data.repositories;

import net.tysonsorensen.Medici.data.entities.BuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyRepository extends JpaRepository<BuyEntity, Integer> {
  List<BuyEntity> findAllByOrderByPriceDesc();
}
