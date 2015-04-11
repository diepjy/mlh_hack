package models;
/**
 * Created by joanna on 11/04/15.
 */

import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import play.Logger;
import play.db.DB;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Entity
public class EventsDB extends Model {

//    DataSource ds = DB.getDataSource();

    @Id
    public Long id;

    public String text;  // you can add annotations like @Required, @ManyToMany etc

    // other attributes (columns)

    public static void create(EventsDB e){
        e.save();
    }

    public static void createNewEvent(EventsDB e, JsonNode json) {
        try {
            Connection connection = DB.getConnection();
            Statement s = connection.createStatement();

            String eventName = json.findPath("eventName").textValue();
            String locationName = json.findPath("locationName").textValue();
            String address = json.findPath("address").textValue();
            String postcode = json.findPath("postcode").textValue();
            String link = json.findPath("link").textValue();
            String startDate = json.findPath("startDate").textValue();
            String endDate = json.findPath("endDate").textValue();
            String timeStart = json.findPath("timeStart").textValue();
            String timeEnd = json.findPath("timeEnd").textValue();

            String insertQuery = "INSERT INTO \"EVENTS_TABLE\" VALUES(" +
                    eventName + "," + locationName
                    + "," + address + "," + postcode + "," + link + "," + startDate + "," + endDate + "," +
                    timeStart + "," + timeEnd + ")";

//            String sql = "INSERT INTO "

            Logger.debug(insertQuery);

            s.executeUpdate(insertQuery);

            Logger.debug("executed the update");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

}
