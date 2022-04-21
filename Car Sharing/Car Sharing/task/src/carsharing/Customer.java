package carsharing;

import java.util.Objects;

public class Customer {
    private int id;
    private String name;
    private Integer renterCarId;

    public Customer(int id, String name, Integer renterCarId) {
        this.id = id;
        this.name = name;
        this.renterCarId = renterCarId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRenterCarId(Integer carId) {
        renterCarId = carId;
    }

    public Integer getRenterCarId() {
        return renterCarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", renterCarId=" + renterCarId +
                '}';
    }
}
