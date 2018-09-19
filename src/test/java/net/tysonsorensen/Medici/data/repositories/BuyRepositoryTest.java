package net.tysonsorensen.Medici.data.repositories;

import net.tysonsorensen.Medici.data.entities.BuyEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyRepositoryTest {

  private static final double DELTA = 0.0001;

  @Autowired
  private BuyRepository uut;

  @Before
  public void setUp() {
    BuyEntity one = new BuyEntity(1, 1.1);
    BuyEntity two = new BuyEntity(3, 1.3);
    BuyEntity three = new BuyEntity(2, 1.2);

    uut.save(one);
    uut.save(two);
    uut.save(three);
  }

  @After
  public void tearDown() {
    uut.deleteAll();
  }

  @Test
  public void findAllByOrderByPriceDesc() {
    List<BuyEntity> result = uut.findAllByOrderByPriceDesc();

    assertEquals(3, result.size());
    assertEquals(3, result.get(0).getQuantity());
    assertEquals(1.3, result.get(0).getPrice(), DELTA);
    assertEquals(2, result.get(1).getQuantity());
    assertEquals(1.2, result.get(1).getPrice(), DELTA);
    assertEquals(1, result.get(2).getQuantity());
    assertEquals(1.1, result.get(2).getPrice(), DELTA);
  }

  @Test
  public void update() {
    BuyEntity buy = uut.findAllByOrderByPriceDesc().get(0);
    buy.setQuantity(5);

    uut.save(buy);
    BuyEntity result = uut.findAllByOrderByPriceDesc().get(0);

    assertEquals(5, result.getQuantity());
    assertEquals(1.3, result.getPrice(), DELTA);
  }
}