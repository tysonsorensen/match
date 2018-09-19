package net.tysonsorensen.Medici.data.entities;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class SellEntity extends BookEntity {
  public SellEntity(int quantity, double price) {
    super(quantity, price);
  }
}
