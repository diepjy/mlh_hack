package controllers;

import controllers.database.Neo4JController;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        Runnable r = new Runnable() {
            public void run() {
                Neo4JController.removeOldEvents();
            }
        };
        new Thread(r).start();
        return ok(index.render());
    }

}
