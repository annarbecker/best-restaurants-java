import java.util.Map;
import java.util.HashMap;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/welcome", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String enteredUsername = request.queryParams("username");
      String enteredPassword = request.queryParams("password");
      User newUser = User.search(enteredUsername);
      model.put("enteredPassword", enteredPassword);
      model.put("user", newUser);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/signup", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/signup.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String enteredUsername = request.queryParams("username");
      String enteredPassword = request.queryParams("password");
      User newUser = new User(enteredUsername, enteredPassword, "user");
      newUser.save();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/adminHome", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("cuisines", Cuisine.all());
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/adminHome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String userCuisine = request.queryParams("type");
      Cuisine newCuisine = new Cuisine(userCuisine);
      newCuisine.save();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/cuisines.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/cuisines.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/restaurants.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      String userReview = request.queryParams("review");
      int userRating = Integer.parseInt(request.queryParams("rating"));
      Review newReview = new Review(user.getUsername(), userReview, userRating, restaurant.getId(), user.getId());
      newReview.save();
      model.put("reviews", restaurant.getReviews());
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      model.put("images", restaurant.getImages());
      model.put("reviews", restaurant.getReviews());
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/adminHome", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String userRestaurant = request.queryParams("name");
      Restaurant newRestaurant = new Restaurant(userRestaurant, Integer.parseInt(request.queryParams("cuisineSelect")));
      newRestaurant.save();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("restaurants", Restaurant.all());
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/adminHome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/cuisines/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("restaurants", cuisine.getRestaurants());
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id/delete", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      restaurant.delete();
      restaurant.deleteReviews();
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/deleteRestaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id/update", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/updateRestaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/restaurants/:id/updated", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String updatedName = request.queryParams("update");
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      restaurant.update(updatedName);
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/updatedRestaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/cuisines/:id/delete", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      cuisine.delete();
      cuisine.deleteRestaurants();
      model.put("user", user);
      model.put("cuisine", cuisine);
      model.put("template", "templates/deleteCuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/cuisines/:id/update", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("cuisine", cuisine);
      model.put("template", "templates/updateCuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/cuisines/:id/updated", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String updatedName = request.queryParams("update");
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      cuisine.update(updatedName);
      model.put("user", user);
      model.put("cuisine", cuisine);
      model.put("template", "templates/updatedCuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id/:reviewId/delete", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Review review = Review.find(Integer.parseInt(request.params(":reviewId")));
      review.delete();
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("review", review);
      model.put("template", "templates/deleteReview.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/users", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("users", User.all());
      model.put("template", "templates/users.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/addAdmin", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("template", "templates/addAdmin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/adminAdded", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      String enteredUsername = request.queryParams("username");
      String enteredPassword = request.queryParams("password");
      User newUser = new User(enteredUsername, enteredPassword, "admin");
      newUser.save();
      model.put("user", user);
      model.put("admin", newUser);
      model.put("template", "templates/adminAdded.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/users/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      User viewedUser = User.find(Integer.parseInt(request.params(":id")));
      model.put("user", user);
      model.put("viewedUser", viewedUser);
      model.put("template", "templates/user.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/users/:id/delete", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      User otherUser = User.find(Integer.parseInt(request.params(":id")));
      otherUser.delete();
      model.put("user", user);
      model.put("useer", otherUser);
      model.put("template", "templates/deleteUser.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/users/:id/update", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      User otherUser = User.find(Integer.parseInt(request.params(":id")));
      model.put("user", user);
      model.put("useer", otherUser);
      model.put("template", "templates/updateUser.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/users/:id/updated", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String updatedPermission = request.queryParams("permission");
      User user = User.find(Integer.parseInt(request.params(":userId")));
      User otherUser = User.find(Integer.parseInt(request.params(":id")));
      otherUser.update(updatedPermission);
      model.put("user", user);
      model.put("useer", otherUser);
      model.put("template", "templates/updatedUser.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id/addImage", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/addImage.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/:userId/restaurants/:id/imageAdded", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String url = request.queryParams("url");
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      Image newImage = new Image(url, restaurant.getId());
      newImage.save();
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/imageAdded.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:userId/restaurants/:id/images/:imageId/delete", (request, reponse) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      Image image = Image.find(Integer.parseInt(request.params(":imageId")));
      image.delete();
      model.put("image", image);
      model.put("user", user);
      model.put("restaurant", restaurant);
      model.put("template", "templates/deletedImage.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
