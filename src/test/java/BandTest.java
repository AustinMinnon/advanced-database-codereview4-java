import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.junit.*;
import static org.junit.Assert.*;

public class BandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Band.all().size(), 0);
  }
  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Band firstBand = new Band("August Burns Red");
    Band secondBand = new Band("August Burns Red");
    assertTrue(firstBand.equals(secondBand));
  }
  @Test
  public void save_savesIntoDatabase_true() {
    Band myBand = new Band("The Ramones");
    myBand.save();
    assertTrue(Band.all().get(0).equals(myBand));
  }

  @Test
  public void find_findBandInDatabase_true() {
    Band myBand = new Band("The Ramones");
    myBand.save();
    Band savedBand = Band.find(myBand.getId());
    assertTrue(myBand.equals(savedBand));
  }

  @Test
  public void addVenue_addsVenueToBand() {
    Band myBand = new Band("The Ramones");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myBand.addVenue(myVenue);
    Venue savedVenue = myBand.getVenues().get(0);
    assertTrue(myVenue.equals(savedVenue));
  }

  @Test
  public void getVenues_returnsAllVenues_ArrayList() {
    Band myBand = new Band("August Burns Red");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myBand.addVenue(myVenue);
    List savedVenues = myBand.getVenues();
    assertEquals(savedVenues.size(), 1);
  }

  @Test
  public void delete_deletesAllVenuesAndListsAssoicationes() {
    Band myBand = new Band("August Burns Red");
    myBand.save();

    Venue myVenue = new Venue("Modacenter");
    myVenue.save();

    myBand.addVenue(myVenue);
    myBand.delete();
    assertEquals(myVenue.getBands().size(), 0);
  }
}
