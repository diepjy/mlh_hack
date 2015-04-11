package models.jdbc.executor;

import java.sql.*;
import java.util.*;

/**
 * Created by joanna on 07/11/14.
 */
public class JdbcNeo4JCypherExecutor implements CypherExecutor {

    private final Connection connection;

    public JdbcNeo4JCypherExecutor(String url) {
        this(url, null, null);
    }
    public JdbcNeo4JCypherExecutor(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url.replace("http://", "jdbc:neo4j://"), username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Map<String, Object>> query(String query, Map<String, Object> params) {
        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            setParameters(statement, params);
            final ResultSet result = statement.executeQuery();
            return new Iterator<Map<String, Object>>() {

                boolean hasNext = result.next();
                public List<String> columns;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                private List<String> getColumns() throws SQLException {
                    if (columns != null) return columns;
                    ResultSetMetaData metaData = result.getMetaData();
                    int count = metaData.getColumnCount();
                    List<String> cols = new ArrayList<String>(count);
                    for (int i = 1; i <= count; i++) cols.add(metaData.getColumnName(i));
                    return columns = cols;
                }

                @Override
                public Map<String, Object> next() {
                    try {
                        if (hasNext) {
                            Map<String, Object> map = new LinkedHashMap<String, Object>();
                            for (String col : getColumns()) map.put(col, result.getObject(col));
                            hasNext = result.next();
                            if (!hasNext) {
                                result.close();
                                statement.close();
                            }
                            return map;
                        } else throw new NoSuchElementException();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void remove() {
                }
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(PreparedStatement statement, Map<String, Object> params) throws SQLException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            int index = Integer.parseInt(entry.getKey());
            statement.setObject(index, entry.getValue());
        }
    }

}
