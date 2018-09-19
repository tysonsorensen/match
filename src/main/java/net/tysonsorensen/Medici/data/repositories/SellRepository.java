package net.tysonsorensen.Medici.data.repositories;

import net.tysonsorensen.Medici.data.entities.SellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellRepository extends JpaRepository<SellEntity, Integer> {
  List<SellEntity> findAllByOrderByPriceAsc();
}
