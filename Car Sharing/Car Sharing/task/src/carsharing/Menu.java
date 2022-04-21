package carsharing;

import java.util.List;
import java.util.NoSuchElementException;

public class Menu extends MenuBase {
    public Menu() {
        super.optionsString =
                "1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "9. Drop tables\n" +
                "0. Exit\n";
    }

    @Override
    boolean handleOption(int option) {
        switch (option) {
            case 0: return false;
            case 1:
                new ManagerMenu().run();
                break;
            case 2:
                new CustomerAuthMenu().run();
                break;
            case 3:
                new CustomerAuthMenu().createCustomer();
                break;
            case 9:
                H2Utils.dropTables();
                break;
            default: break;
        }
        return true;
    }

}

class CustomerAuthMenu extends MenuBase {
    CustomerDao customerDao = new CustomerDaoH2();
    List<Customer> customers;

    public CustomerAuthMenu() {
        customerDao.getAllCustomers().ifPresentOrElse(
                list -> {
                    customers = list;
                    StringBuilder optionsStringBuilder = new StringBuilder("Customer list:\n");
                    for(int i = 0; i < customers.size(); ++i)
                        optionsStringBuilder.append(i + 1).append(". ").append(customers.get(i).getName()).append("\n");
                    optionsStringBuilder.append("0. Back\n");
                    super.optionsString = optionsStringBuilder.toString();
                },
                () -> {
                    super.runnable = false;
                    System.out.println("The customer list is empty!\n");
                }
        );
    }

    @Override
    boolean handleOption(int option) {
        if(option > 0 && option <= customers.size())
            new CustomerMenu(customers.get(option-1)).run();
        return false;
    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        if(customerDao.addCustomer(name))
            System.out.println("The customer was added!\n");
        else
            System.out.println("Oops, something went wrong!");
    }
}

class CustomerMenu extends MenuBase {
    Customer customer;
    CustomerDao customerDao = new CustomerDaoH2();

    public CustomerMenu(Customer customer) {
        this.customer = customer;
        super.optionsString =
                "1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back\n";
    }

    @Override
    boolean handleOption(int option) {
        switch (option) {
            case 0: return false;
            case 1:
                rentCar();
                break;
            case 2:
                returnCar();
                break;
            case 3:
                printRentedCar();
                break;
            default: break;
        }
        return true;
    }

    private void rentCar() {
        if(customer.getRenterCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        List<Company> companies;
        try {
            companies = new CompanyDaoH2().getAllCompanies().orElseThrow();
        } catch (NoSuchElementException e) {
            System.out.println("Error!");
            return;
        }
        System.out.println("Choose a company:");
        for(int i = 0; i < companies.size(); ++i)
            System.out.println((i+1) + ". " + companies.get(i).getName());
        System.out.println("0. Back");

        int choice = getRangedInput(0, companies.size());
        if(choice == 0) return;
        int companyIndex = choice-1;

        List<Car> cars = new CarDaoH2().getFreeCarList(companies.get(companyIndex).getId()).orElse(null);
        if(cars == null || cars.isEmpty()) {
            System.out.printf("No available cars in the '%s' company\n", companies.get(companyIndex).getName());
            return;
        }
        System.out.println("Choose a car:");
        for(int i = 0; i < cars.size(); ++i)
            System.out.println((i+1) + ". " + cars.get(i).getName());
        System.out.println("0. Back");

        choice = getRangedInput(0, companies.size());
        if(choice == 0) return;
        int carIndex = choice-1;

        if(customerDao.setRentedCarId(customer, cars.get(carIndex).getId()))
            System.out.printf("You rented '%s'\n", cars.get(carIndex).getName());
        else
            System.out.println("Oops, something went wrong!");
    }
    private void returnCar() {
        if(customer.getRenterCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        if(customerDao.setRentedCarId(customer, null))
            System.out.println("You've returned a rented car!");
        else
            System.out.println("Oops, something went wrong!");
    }

    private void printRentedCar() {
        if(customer.getRenterCarId() != null){
            Car car;
            try{
                car = new CarDaoH2().getCarById(customer.getRenterCarId()).orElseThrow();
            } catch(NoSuchElementException e) {
                System.out.println("Error!");
                return;
            }
            Company company;
            try{
                company = new CompanyDaoH2().getCompanyById(car.getCompanyId()).orElseThrow();
            } catch(NoSuchElementException e) {
                System.out.println("Error!");
                return;
            }
            System.out.println("Your rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        } else System.out.println("You didn't rent a car!");
    }
}

class ManagerMenu extends MenuBase {
    CompanyDao companyDao = new CompanyDaoH2();

    public ManagerMenu() {
        super.optionsString =
                "1. Company list\n" +
                "2. Create a company\n" +
                "0. Back\n";
    }

    @Override
    boolean handleOption(int option) {
        switch (option) {
            case 0: return false;
            case 1:
                new CompanyListMenu().run();
                break;
            case 2:
                createCompany();
                break;
            default: break;
        }
        return true;
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        if(companyDao.addCompany(name))
            System.out.println("The company was created!\n");
        else
            System.out.println("Oops, something went wrong!");
    }
}

class CompanyListMenu extends MenuBase {
    CompanyDao companyDao = new CompanyDaoH2();
    List<Company> companies = null;

    public CompanyListMenu() {
        companyDao.getAllCompanies().ifPresentOrElse(
                list->{
                    companies = list;
                    StringBuilder optionsStringBuilder = new StringBuilder("Choose the company:\n");
                    for(int i = 0; i < companies.size(); ++i)
                        optionsStringBuilder.append(i + 1).append(". ").append(companies.get(i).getName()).append("\n");
                    optionsStringBuilder.append("0. Back\n");
                    super.optionsString = optionsStringBuilder.toString();
                },
                () -> {
                    System.out.println("The company list is empty!\n");
                    super.runnable = false;
                }
        );
    }

    @Override
    boolean handleOption(int option) {
        if(option == 0) return false;
        if(option <= companies.size())
            new CompanyMenu(companies.get(option-1)).run();
        return false;
    }
}

class CompanyMenu extends MenuBase {

    Company company;
    CarDao carDao = new CarDaoH2();

    public CompanyMenu(Company company) {
        this.company = company;
        super.optionsString =
                "'"+company.getName()+"' company:\n" +
                "1. Car list\n" +
                "2. Create a car\n" +
                "0. Back\n";
    }

    @Override
    boolean handleOption(int option) {
        switch (option) {
            case 0: return false;
            case 1:
                printCarList();
                break;
            case 2:
                createCar();
                break;
            default: break;
        }
        return true;
    }

    private void createCar() {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        if(carDao.addCar(name, company.getId()))
            System.out.println("The car was added!");
        else
            System.out.println("Oops, something went wrong!");
    }

    private void printCarList() {
        carDao.getAllCar(company.getId()).ifPresentOrElse(
                cars -> {
                    System.out.println("Car list:");
                    for(int i = 0; i < cars.size(); ++i)
                        System.out.println((i+1) + ". " + cars.get(i).getName());
                },
                () -> System.out.println("The car list is empty!")
        );
    }
}