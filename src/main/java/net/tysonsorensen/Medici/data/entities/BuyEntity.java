package net.tysonsorensen.Medici.data.entities;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class BuyEntity extends BookEntity {

  public BuyEntity(int quantity, double price) {
    super(quantity, price);
  }
}
