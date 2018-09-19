package net.tysonsorensen.Medici.service;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.Medici.data.entities.BuyEntity;
import net.tysonsorensen.Medici.data.entities.SellEntity;
import net.tysonsorensen.Medici.data.repositories.BuyRepository;
import net.tysonsorensen.Medici.data.repositories.SellRepository;
import net.tysonsorensen.Medici.service.models.BookModel;
import net.tysonsorensen.Medici.service.models.BuyModel;
import net.tysonsorensen.Medici.service.models.SellModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

  private final BuyRepository buyRepository;
  private final SellRepository sellRepository;

  private static BuyModel convertBuyModel(final BuyEntity buy) {
    return new BuyModel(buy.getQuantity(), buy.getPrice());
  }

  private static SellModel convertSellModel(final SellEntity sell) {
    return new SellModel(sell.getQuantity(), sell.getPrice());
  }

  /**
   * Returns lists of requests waiting for a match.
   * Lists Buy and Sell requests, empty lists if there are no unmatched requests.
   * Buy list is sorted by highest price first.
   * Sell list is sorted by lowest price first.
   * An audit trail is left in the data layer, so requests aren't removed, just left with
   * zero as the quantity. The zero requests are filtered out before data is sent to the user.
   *
   * @return BookModel containing two lists
   */
  public BookModel getBooks() {
    BookModel books = new BookModel();
    books.setBuys(buyRepository.findAllByOrderByPriceDesc().stream()
        .map(MatchService::convertBuyModel)
        .filter(b -> b.getQty() > 0)
        .collect(Collectors.toList()));
    books.setSells(sellRepository.findAllByOrderByPriceAsc().stream()
        .map(MatchService::convertSellModel)
        .filter(s -> s.getQty() > 0)
        .collect(Collectors.toList()));
    return books;
  }

  /**
   * Adds the new buy request to the data layer and looks for a matching sell request.
   * If it matches with a sell the data layer will be updated for both requests.
   *
   *
   * @param buy The quantity desired and price willing to pay.
   */
  public void buy(BuyModel buy) {
    List<BuyEntity> buys = buyRepository.findAllByOrderByPriceDesc();
    List<SellEntity> sells = sellRepository.findAllByOrderByPriceAsc();
    buys = addNewBuy(buys, buy);
    processBuySell(buys, sells);
  }

  /**
   * Adds the new buy request to the data layer and looks for a matching sell request.
   * If it matches with a sell the data layer will be updated for both requests.
   *
   * @param sell The quantity desired and price willing to pay.
   */
  public void sell(final SellModel sell) {
    List<BuyEntity> buys = buyRepository.findAllByOrderByPriceDesc();
    List<SellEntity> sells = sellRepository.findAllByOrderByPriceAsc();
    sells = addNewSell(sells, sell);
    processBuySell(buys, sells);
  }

  private List<BuyEntity> addNewBuy(final List<BuyEntity> buys, final BuyModel buy) {
    boolean found = false;
    //looks to see if this price point is already in db
    for(BuyEntity buyEntity : buys) {
      int price1 = (int)(buy.getPrc() * 100);
      int price2 = (int)(buyEntity.getPrice() * 100);
      if (price1 == price2) {
        found = true;
        buyEntity.setQuantity(buyEntity.getQuantity() + buy.getQty());
        break;
      }
    }
    if (!found) {
      buys.add(new BuyEntity(buy.getQty(), buy.getPrc()));
    }
    return buys;
  }

  private List<SellEntity> addNewSell(final List<SellEntity> sells, final SellModel sell) {
    boolean found = false;
    //looks to see if this price point is already in db
    for(SellEntity sellEntity : sells) {
      int price1 = (int)(sell.getPrc() * 100);
      int price2 = (int)(sellEntity.getPrice() * 100);
      if (price1 == price2) {
        found = true;
        sellEntity.setQuantity(sellEntity.getQuantity() + sell.getQty());
        break;
      }
    }
    if (!found) {
      sells.add(new SellEntity(sell.getQty(), sell.getPrc()));
    }
    return sells;
  }

  @Transactional //allows rollback if one of the queries fail, to keep data integrity
  private void processBuySell(List<BuyEntity> buys, List<SellEntity> sells) {
    for(BuyEntity b : buys) {
      for (SellEntity s : sells) {
        if (b.getQuantity() > 0 && s.getQuantity() > 0) {
          if (b.getPrice() >= s.getPrice()) {
            int sellQuantity = s.getQuantity();
            int buyQuantity = b.getQuantity();
            if (buyQuantity == sellQuantity) {
              s.setQuantity(0);
              b.setQuantity(0);
              break;
            } else if (buyQuantity < sellQuantity) {
              s.setQuantity(sellQuantity - buyQuantity);
              b.setQuantity(0);
              break;
            } else {
              b.setQuantity(buyQuantity - sellQuantity);
              s.setQuantity(0);
            }
          }
        }
      }
    }
    buyRepository.saveAll(buys.stream().filter(b -> b.getQuantity() > 0).collect(Collectors.toList()));
    sellRepository.saveAll(sells.stream().filter(s -> s.getQuantity() > 0).collect(Collectors.toList()));
  }

}
