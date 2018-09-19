package net.tysonsorensen.Medici.service.models;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BuyModel extends BaseModel {
  public BuyModel(int quantity, double price) {
    super(quantity, price);
  }
}
