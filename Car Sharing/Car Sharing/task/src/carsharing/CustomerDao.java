package carsharing;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    Optional<List<Customer>> getAllCustomers();
    boolean addCustomer(String name);
    boolean setRentedCarId(Customer customer, Integer carId);
}
