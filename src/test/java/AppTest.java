import org.junit.*;
import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Welcome to the official Tour and Venue Directory");
  }

  @Test
  public void BandIsCreatedTest() {
    goTo("http://localhost:4567/");
    fill("#name").with("Modest Mouse");
    submit(".bandbtn");
    assertThat(pageSource()).contains("Modest Mouse");
  }

  @Test
  public void VenueIsCreatedTest() {
    goTo("http://localhost:4567/");
    fill("#name").with("Modacenter");
    submit(".venuebtn");
    assertThat(pageSource()).contains("Modacenter");
  }

  @Test
  public void deleteAllRemovesBandsTest() {
    Band testBand = new Band("3 Doors Down");
    testBand.save();
    goTo("http://localhost:4567/");
    submit(".allbands");
    assertThat(pageSource()).doesNotContain("3 Doors Down");
  }

  @Test
  public void deleteAllRemovesVenuesTest() {
    Venue testVenue = new Venue("Modacenter");
    testVenue.save();
    goTo("http://localhost:4567/");
    submit(".allvenues");
    assertThat(pageSource()).doesNotContain("Modacenter");
  }

  @Test
  public void bandUpdateNameTest() {
    Band testBand = new Band("Skillet");
    testBand.save();
    String bandpage = String.format("http://localhost:4567/band/%d", testBand.getId());
    goTo(bandpage);
    fill("#name").with("RED");
    submit(".updateband");
    assertThat(pageSource()).contains("RED");
  }

  @Test
  public void venueUpdateNameTest() {
    Venue testVenue = new Venue("Modacenter");
    testVenue.save();
    String venuepage = String.format("http://localhost:4567/venue/%d", testVenue.getId());
    goTo(venuepage);
    fill("#name").with("MoodyCenter");
    submit(".updatevenue");
    assertThat(pageSource()).contains("MoodyCenter");
  }

  @Test
  public void bandDeleteTest() {
    Band testBand = new Band("sixx am");
    testBand.save();
    String bandpage = String.format("http://localhost:4567/bands/%d", testBand.getId());
    goTo(bandpage);
    submit(".deleteband");
    assertThat(pageSource()).doesNotContain("sixx am");
  }

  @Test
  public void venueDeleteTest() {
    Venue testVenue = new Venue("Modacenter");
    testVenue.save();
    String venuepage = String.format("http://localhost:4567/venues/%d", testVenue.getId());
    goTo(venuepage);
    submit(".deletevenue");
    assertThat(pageSource()).doesNotContain("Modacenter");
  }
}
