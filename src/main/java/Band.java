import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang.WordUtils;

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

  public void firstToUppercase() {
    this.name = WordUtils.capitalize(this.name.toLowerCase());
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

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE bands SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
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

  public ArrayList<Venue> getVenues() {
      try(Connection con = DB.sql2o.open()){
        String sql = "SELECT venue_id FROM bands_venues WHERE band_id = :band_id";
        List<Integer> venueIds = con.createQuery(sql)
          .addParameter("band_id", this.getId())
          .executeAndFetch(Integer.class);

        ArrayList<Venue> venues = new ArrayList<Venue>();

        for (Integer venueId : venueIds) {
            String venueQuery = "SELECT * FROM venues WHERE id = :venueId";
            Venue venue = con.createQuery(venueQuery)
              .addParameter("venueId", venueId)
              .executeAndFetchFirst(Venue.class);
            venues.add(venue);
        }
        return venues;
      }
    }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM bands WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM bands_venues WHERE band_id = :bandId";
        con.createQuery(joinDeleteQuery)
          .addParameter("bandId", this.getId())
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
