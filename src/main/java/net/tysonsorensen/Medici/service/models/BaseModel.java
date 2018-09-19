package net.tysonsorensen.Medici.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel {
  @Getter @Setter
  private int qty;

  @Getter @Setter
  private double prc;
}
