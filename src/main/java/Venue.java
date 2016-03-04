import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Venue {
  private int id;
  private String name;
  private String description;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Venue(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherVenue){
    if (!(otherVenue instanceof Venue)) {
      return false;
    } else {
      Venue newVenue = (Venue) otherVenue;
      return this.getName().equals(newVenue.getName()) &&
      this.getId() == newVenue.getId();
    }
  }

  public static List<Venue> all() {
    String sql = "SELECT * FROM venues";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Venue.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO venues(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Venue find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues where id=:id";
      Venue venue = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Venue.class);
      return venue;
    }
  }

  public void addBand(Band band) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, author_id) VALUES (:book_id, :venue_id)";
      con.createQuery(sql)
      .addParameter("venue_id", this.getId())
      .addParameter("band_id", band.getId())
      .executeUpdate();
    }
  }

  public List<Band> getBands() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT bands.* FROM venues JOIN bands_venues ON (venues.id = bands_venues.venue_id) JOIN bands ON (bands_venues.band_id = bands.id) WHERE venues.id = :venue_id;";
      return con.createQuery(sql)
      .addParameter("venue_id", this.getId())
      .executeAndFetch(Band.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM venues WHERE id =:id;";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void deleteAllVenues() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE * FROM venues;";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    }
  }
}
