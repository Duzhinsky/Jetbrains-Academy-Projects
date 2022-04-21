package carsharing;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    Optional<List<Car>> getAllCar(int companyId);
    Optional<List<Car>> getFreeCarList(int companyId);
    Optional<Car> getCarById(int carId);
    boolean addCar(String name, int companyId);
}
