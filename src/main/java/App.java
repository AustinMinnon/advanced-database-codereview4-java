import java.util.HashMap;
import java.util.List;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public/");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add/band", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String inputName = request.queryParams("name");
      Band newBand = new Band(inputName);
      newBand.firstToUppercase();
      newBand.save();
      model.put("band", newBand);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add/venue", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String inputName = request.queryParams("name");
      Venue newVenue = new Venue(inputName);
      newVenue.firstToUppercase();
      newVenue.save();
      model.put("venue", newVenue);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/band/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      model.put("band", myBand);
      model.put("template", "templates/band.vtl");
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/band/update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      int venueId = Integer.parseInt(request.queryParams("venue_id"));
      Venue myVenue = Venue.find(venueId);
      myBand.addVenue(myVenue);
      model.put("band", myBand);
      model.put("template", "templates/band.vtl");
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    post("/band/update/name/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      String inputName = request.queryParams("name");
      myBand.update(inputName);
      model.put("band", myBand);
      model.put("venues", Venue.all());

      response.redirect("/");
      return null;
    });

    post("/band/deleteALL/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      myBand.delete();
      model.put("band", myBand);
        response.redirect("/");
        return null;
      });

    post("/band/delete/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band myBand = Band.find(id);
      myBand.delete();
      model.put("band", myBand);
        response.redirect("/");
        return null;
      });

    post("/band/deleteAll", (request, response) -> {
      Band.deleteAllBands();
      response.redirect("/");
      return null;
    });

    get("/venue/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue myVenue = Venue.find(id);
      model.put("venue", myVenue);
      model.put("template", "templates/venue.vtl");
      model.put("bands", Band.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/venue/update/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue myVenue = Venue.find(id);
      int bandId = Integer.parseInt(request.queryParams("band_id"));
      Band myBand = Band.find(bandId);
      myVenue.addBand(myBand);
      model.put("venue", myVenue);
      model.put("template", "templates/venue.vtl");
      model.put("bands", Band.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/venue/update/name/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue myVenue = Venue.find(id);
      String inputName = request.queryParams("name");
      myVenue.update(inputName);
      model.put("venue", myVenue);
      model.put("bands", Band.all());
      response.redirect("/");
      return null;
    });

    post("/venue/deleteALL/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue myVenue = Venue.find(id);
      myVenue.delete();
      model.put("venue", myVenue);
      response.redirect("/");
      return null;
    });

    post("/venue/delete/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue myVenue = Venue.find(id);
      myVenue.delete();
      model.put("venue", myVenue);
      response.redirect("/");
      return null;
    });

    post("/venue/deleteAll", (request, response) -> {
      Venue.deleteAllVenues();
      response.redirect("/");
      return null;
    });

  }
}
