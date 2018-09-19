package net.tysonsorensen.Medici.rest;

import net.tysonsorensen.Medici.service.MatchService;
import net.tysonsorensen.Medici.service.models.BookModel;
import net.tysonsorensen.Medici.service.models.BuyModel;
import net.tysonsorensen.Medici.service.models.SellModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MatchControllerTest {

  private MatchService mockMatchService = mock(MatchService.class);
  private MatchController uut = new MatchController(mockMatchService);

  @Test
  public void book() {
    doReturn(new BookModel()).when(mockMatchService).getBooks();

    ResponseEntity result = uut.book();

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void sell() {
    ResponseEntity result = uut.sell(new SellModel());

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void buy() {
    ResponseEntity result = uut.buy(new BuyModel());

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }
}