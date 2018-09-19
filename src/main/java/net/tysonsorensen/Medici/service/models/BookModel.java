package net.tysonsorensen.Medici.service.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookModel {

  @Getter @Setter
  List<BuyModel> buys;

  @Getter @Setter
  List<SellModel> sells;

}
