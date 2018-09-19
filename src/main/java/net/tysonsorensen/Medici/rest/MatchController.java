package net.tysonsorensen.Medici.rest;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.Medici.service.MatchService;
import net.tysonsorensen.Medici.service.models.BuyModel;
import net.tysonsorensen.Medici.service.models.SellModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchController {

  private final MatchService matchService;

  /**
   * Rest endpoint for getting a report of existing buy and sell waiting for a match.
   *
   * @return ResponseEntity with a BookModel as the body. (List of BuyModels and SellModels)
   */
  @GetMapping(path = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity book() {
    return ResponseEntity.ok(matchService.getBooks());
  }


  /**
   * Rest endpoint for submitting a sell request.
   *
   * @param sell a sell request consisting of quantity and price
   * @return ResponseEntity with an empty body.
   */
  @PostMapping(path = "/sell", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity sell(@RequestBody SellModel sell) {
    matchService.sell(sell);
    return ResponseEntity.ok().build();
  }

  /**
   * Rest endpoint for submitting a buy request.
   *
   * @param buy a buy request consisting of quantity and price
   * @return ResponseEntity with an empty body.
   */
  @PostMapping(path = "/buy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity buy(@RequestBody BuyModel buy) {
    matchService.buy(buy);
    return ResponseEntity.ok().build();
  }

}
