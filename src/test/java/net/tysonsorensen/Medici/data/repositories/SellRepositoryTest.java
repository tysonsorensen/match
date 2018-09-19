package net.tysonsorensen.Medici.data.repositories;

import net.tysonsorensen.Medici.data.entities.SellEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellRepositoryTest {

  private static final double DELTA = 0.0001;

  @Autowired
  private SellRepository uut;

  @Before
  public void setUp()  {
    SellEntity one = new SellEntity(1, 1.1);
    SellEntity two = new SellEntity(3, 1.3);
    SellEntity three = new SellEntity(2, 1.2);

    uut.save(one);
    uut.save(two);
    uut.save(three);
  }

  @After
  public void tearDown() {
    uut.deleteAll();
  }

  @Test
  public void findAllByOrderByPriceDesc(){
    List<SellEntity> result = uut.findAllByOrderByPriceAsc();

    assertEquals(3, result.size());
    assertEquals(1, result.get(0).getQuantity());
    assertEquals(1.1, result.get(0).getPrice(), DELTA);
    assertEquals(2, result.get(1).getQuantity());
    assertEquals(1.2, result.get(1).getPrice(), DELTA);
    assertEquals(3, result.get(2).getQuantity());
    assertEquals(1.3, result.get(2).getPrice(), DELTA);
  }

  @Test
  public void update() {
    SellEntity sell = uut.findAllByOrderByPriceAsc().get(0);
    sell.setQuantity(5);

    uut.save(sell);
    SellEntity result = uut.findAllByOrderByPriceAsc().get(0);

    assertEquals(5, result.getQuantity());
    assertEquals(1.1, result.getPrice(), DELTA);
  }
}