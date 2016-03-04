import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Band {
  public int id;
  public String name;
  public String description;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Band(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherBand){
    if (!(otherBand instanceof Band)) {
      return false;
    } else {
      Band newBand = (Band) otherBand;
      return this.getName().equals(newBand.getName()) &&
            this.getId() == newBand.getId();
    }
  }

  public static List<Band> all() {
    String sql = "SELECT * FROM bands";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Band.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Band find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands where id=:id";
      Band band = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Band.class);
      return band;
    }
  }

  public void addVenue(Venue venue) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues(band_id, venue_id) VALUES (:band_id, :venue_id)";
      con.createQuery(sql)
      .addParameter("band_id", this.getId())
      .addParameter("venue_id", venue.getId())
      .executeUpdate();
    }
  }

  public List<Venue> getVeneues() {
    try(Connection con= DB.sql2o.open()){
      String sql= "SELECT venues.* FROM bands JOIN bands_venues ON (bands.id = bands_venues.band_id) JOIN venues ON (bands_venues.venue_id = venue.id) WHERE bands.id = :band_id;";
      return con.createQuery(sql)
      .addParameter("band_id", this.getId())
      .executeAndFetch(Venue.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM bands WHERE id = :id;";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void deleteAllBands() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE * FROM books;";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    }
  }
}
