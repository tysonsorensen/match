package net.tysonsorensen.Medici.service;

import net.tysonsorensen.Medici.data.entities.BuyEntity;
import net.tysonsorensen.Medici.data.entities.SellEntity;
import net.tysonsorensen.Medici.data.repositories.BuyRepository;
import net.tysonsorensen.Medici.data.repositories.SellRepository;
import net.tysonsorensen.Medici.service.models.BookModel;
import net.tysonsorensen.Medici.service.models.BuyModel;
import net.tysonsorensen.Medici.service.models.SellModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MatchServiceTest {
  private static final double DELTA = 0.00001;

  private BuyRepository mockBuyRepo = mock(BuyRepository.class);
  private SellRepository mockSellRepo = mock(SellRepository.class);
  private MatchService uut = new MatchService(mockBuyRepo, mockSellRepo);

  @Test
  public void getBooks_empty() {
    doReturn(Collections.emptyList()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(Collections.emptyList()).when(mockSellRepo).findAllByOrderByPriceAsc();

    BookModel result = uut.getBooks();

    assertTrue(result.getBuys().isEmpty());
    assertTrue(result.getSells().isEmpty());
  }

  @Test
  public void getBooks() {
    List<BuyEntity> buys = new ArrayList<>();
    buys.add(new BuyEntity(1, 1.1));
    buys.add(new BuyEntity(2, 1.2));
    List<SellEntity> sells = new ArrayList<>();
    sells.add(new SellEntity(1, 1.2));
    sells.add(new SellEntity(2, 1.1));
    doReturn(buys).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(sells).when(mockSellRepo).findAllByOrderByPriceAsc();

    BookModel result = uut.getBooks();

    assertEquals(2, result.getBuys().size());
    assertEquals(2, result.getSells().size());
    assertEquals(1, result.getBuys().get(0).getQty());
    assertEquals(1.1, result.getBuys().get(0).getPrc(), DELTA);
    assertEquals(2, result.getBuys().get(1).getQty());
    assertEquals(1.2, result.getBuys().get(1).getPrc(), DELTA);
    assertEquals(1, result.getSells().get(0).getQty());
    assertEquals(1.2, result.getSells().get(0).getPrc(), DELTA);
    assertEquals(2, result.getSells().get(1).getQty());
    assertEquals(1.1, result.getSells().get(1).getPrc(), DELTA);
  }

  @Test
  public void buy_empty() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.buy(new BuyModel(1, 1.1));

    BookModel validate = uut.getBooks();
    assertEquals(0, validate.getSells().size());
    assertEquals(1, validate.getBuys().size());
    assertEquals(1, validate.getBuys().get(0).getQty());
    assertEquals(1.1, validate.getBuys().get(0).getPrc(), DELTA);
  }

  @Test
  public void buy_samePrice() {
    List<BuyEntity> buys = new ArrayList<>();
    buys.add(new BuyEntity(1, 1.02));
    buys.add(new BuyEntity(1, 1.01));
    buys.add(new BuyEntity(1, 1.00));
    doReturn(buys).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.buy(new BuyModel(1, 1.01));

    BookModel validate = uut.getBooks();
    assertEquals(0, validate.getSells().size());
    assertEquals(3, validate.getBuys().size());
    assertEquals(1, validate.getBuys().get(0).getQty());
    assertEquals(1.02, validate.getBuys().get(0).getPrc(), DELTA);
    assertEquals(2, validate.getBuys().get(1).getQty());
    assertEquals(1.01, validate.getBuys().get(1).getPrc(), DELTA);
    assertEquals(1, validate.getBuys().get(2).getQty());
    assertEquals(1.00, validate.getBuys().get(2).getPrc(), DELTA);
  }

  @Test
  public void sell_empty() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.sell(new SellModel(1, 1.1));

    BookModel validate = uut.getBooks();
    assertEquals(1, validate.getSells().size());
    assertEquals(0, validate.getBuys().size());
    assertEquals(1, validate.getSells().get(0).getQty());
    assertEquals(1.1, validate.getSells().get(0).getPrc(), DELTA);
  }

  @Test
  public void sell_samePrice() {
    List<SellEntity> sells = new ArrayList<>();
    sells.add(new SellEntity(1, 1.02));
    sells.add(new SellEntity(1, 1.01));
    sells.add(new SellEntity(1, 1.00));
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(sells).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.sell(new SellModel(1, 1.01));

    BookModel validate = uut.getBooks();
    assertEquals(3, validate.getSells().size());
    assertEquals(0, validate.getBuys().size());
    assertEquals(1, validate.getSells().get(0).getQty());
    assertEquals(1.02, validate.getSells().get(0).getPrc(), DELTA);
    assertEquals(2, validate.getSells().get(1).getQty());
    assertEquals(1.01, validate.getSells().get(1).getPrc(), DELTA);
    assertEquals(1, validate.getSells().get(2).getQty());
    assertEquals(1.00, validate.getSells().get(2).getPrc(), DELTA);
  }

  @Test
  public void buySell_noMatch() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.buy(new BuyModel(1, 1.00));
    uut.sell(new SellModel(1, 2.00));

    BookModel validate = uut.getBooks();
    assertEquals(1, validate.getSells().size());
    assertEquals(1, validate.getBuys().size());
  }

  @Test
  public void buySell_match() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.buy(new BuyModel(1, 1.00));
    uut.sell(new SellModel(1, 1.00));

    BookModel validate = uut.getBooks();
    assertEquals(0, validate.getSells().size());
    assertEquals(0, validate.getBuys().size());
  }

  @Test
  public void buySell_overflow() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.buy(new BuyModel(2, 1.00));
    uut.sell(new SellModel(1, 0.98));
    uut.sell(new SellModel(1, 0.99));

    BookModel validate = uut.getBooks();
    assertEquals(0, validate.getSells().size());
    assertEquals(0, validate.getBuys().size());
  }

  @Test
  public void buySell_testCase() {
    doReturn(new ArrayList<BuyEntity>()).when(mockBuyRepo).findAllByOrderByPriceDesc();
    doReturn(new ArrayList<SellEntity>()).when(mockSellRepo).findAllByOrderByPriceAsc();

    uut.sell(new SellModel(10, 15.0));
    uut.sell(new SellModel(10, 13.0));
    uut.buy(new BuyModel(10, 7.0));
    uut.buy(new BuyModel(10, 9.5));

    BookModel validate = uut.getBooks();
    validate.getBuys().sort(Collections.reverseOrder(Comparator.comparing(BuyModel::getPrc)));
    validate.getSells().sort(Comparator.comparing(SellModel::getPrc));
    assertEquals(2, validate.getSells().size());
    assertEquals(2, validate.getBuys().size());

    assertEquals(10, validate.getBuys().get(0).getQty());
    assertEquals(9.5, validate.getBuys().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getBuys().get(1).getQty());
    assertEquals(7.0, validate.getBuys().get(1).getPrc(), DELTA);

    assertEquals(10, validate.getSells().get(0).getQty());
    assertEquals(13.0, validate.getSells().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getSells().get(1).getQty());
    assertEquals(15.0, validate.getSells().get(1).getPrc(), DELTA);

    uut.sell(new SellModel(5, 9.5));

    validate = uut.getBooks();
    validate.getBuys().sort(Collections.reverseOrder(Comparator.comparing(BuyModel::getPrc)));
    validate.getSells().sort(Comparator.comparing(SellModel::getPrc));
    assertEquals(2, validate.getSells().size());
    assertEquals(2, validate.getBuys().size());

    assertEquals(5, validate.getBuys().get(0).getQty());
    assertEquals(9.5, validate.getBuys().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getBuys().get(1).getQty());
    assertEquals(7.0, validate.getBuys().get(1).getPrc(), DELTA);

    assertEquals(10, validate.getSells().get(0).getQty());
    assertEquals(13.0, validate.getSells().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getSells().get(1).getQty());
    assertEquals(15.0, validate.getSells().get(1).getPrc(), DELTA);

    uut.buy(new BuyModel(6, 13.0));

    validate = uut.getBooks();
    validate.getBuys().sort(Collections.reverseOrder(Comparator.comparing(BuyModel::getPrc)));
    validate.getSells().sort(Comparator.comparing(SellModel::getPrc));
    assertEquals(2, validate.getSells().size());
    assertEquals(2, validate.getBuys().size());

    assertEquals(5, validate.getBuys().get(0).getQty());
    assertEquals(9.5, validate.getBuys().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getBuys().get(1).getQty());
    assertEquals(7.0, validate.getBuys().get(1).getPrc(), DELTA);

    assertEquals(4, validate.getSells().get(0).getQty());
    assertEquals(13.0, validate.getSells().get(0).getPrc(), DELTA);
    assertEquals(10, validate.getSells().get(1).getQty());
    assertEquals(15.0, validate.getSells().get(1).getPrc(), DELTA);
  }
}