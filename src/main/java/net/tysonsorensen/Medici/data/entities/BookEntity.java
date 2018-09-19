package net.tysonsorensen.Medici.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
@Table(name = "books")
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  @Getter @Setter
  @Column(name = "qty")
  private int quantity;

  @Getter @Setter
  @Column(name = "prc")
  private double price;

  public BookEntity(int quantity, double price) {
    this.price = price;
    this.quantity = quantity;
  }
}
