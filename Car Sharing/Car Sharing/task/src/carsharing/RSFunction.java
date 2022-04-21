package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@FunctionalInterface
public interface RSFunction<T> extends Function<ResultSet, T> {
    @Override
    default T apply(ResultSet resultSet) {
        try {
            return applyThrows(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    T applyThrows(ResultSet resultSet) throws SQLException;
}
