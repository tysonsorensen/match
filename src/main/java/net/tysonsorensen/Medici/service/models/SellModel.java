package net.tysonsorensen.Medici.service.models;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SellModel extends BaseModel {
  public SellModel(int quantity, double price) {
    super(quantity, price);
  }
}
