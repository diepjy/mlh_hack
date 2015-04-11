package models.jdbc.executor;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by joanna on 07/11/14.
 */
public interface CypherExecutor {

    Iterator<Map<String, Object>> query(String statement, Map<String, Object> params);

}
