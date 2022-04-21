package carsharing;

import java.util.List;
import java.util.Optional;

public class CustomerDaoH2 implements CustomerDao {
    @Override
    public Optional<List<Customer>> getAllCustomers() {

        return H2Utils.getObjectList(
                "SELECT * FROM CUSTOMER;",
                rs -> {
                    Integer rentedCarId = rs.getInt("RENTED_CAR_ID");
                    if(rs.wasNull()) rentedCarId = null;
                    return new Customer(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rentedCarId
                    );
                }
        );
    }

    @Override
    public boolean addCustomer(String name) {
        try {
            H2Utils.execute("INSERT INTO CUSTOMER (name) VALUES ('"+name+"');");
        } catch(RuntimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean setRentedCarId(Customer customer, Integer carId) {
        try {
            H2Utils.execute("UPDATE CUSTOMER SET RENTED_CAR_ID="+carId+" WHERE ID="+customer.getId()+";");
            customer.setRenterCarId(carId);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
}
