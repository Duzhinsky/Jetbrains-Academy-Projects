package carsharing;

import java.util.List;
import java.util.Optional;

public class CarDaoH2 implements CarDao {

    @Override
    public Optional<List<Car>> getAllCar(int companyId) {
        return H2Utils.getObjectList(
                "SELECT * FROM CAR WHERE COMPANY_ID="+companyId+";",
                rs -> new Car(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getInt("COMPANY_ID")
                )
        );
    }

    @Override
    public Optional<List<Car>> getFreeCarList(int companyId) {
        return H2Utils.getObjectList(
                "SELECT CAR.* FROM CAR " +
                        "LEFT JOIN CUSTOMER " +
                        "ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                        "WHERE " +
                        "CUSTOMER.RENTED_CAR_ID IS NULL " +
                        "AND CAR.COMPANY_ID="+companyId+";",
                rs -> new Car(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getInt("COMPANY_ID")
                )
        );
    }

    @Override
    public Optional<Car> getCarById(int carId) {
        var listOptional = H2Utils.getObjectList(
                "SELECT * FROM CAR WHERE ID="+carId+" LIMIT 1;",
                rs -> new Car(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getInt("COMPANY_ID")
                )
        );
        if(listOptional.isEmpty()) return Optional.empty();
        var list = listOptional.get();
        if(list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));

    }

    @Override
    public boolean addCar(String name, int companyId) {
        try {
            H2Utils.execute("INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('"+name+"', "+companyId+");");
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
}
