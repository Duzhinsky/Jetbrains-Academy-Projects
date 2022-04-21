package carsharing;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    Optional<List<Company>> getAllCompanies();
    Optional<Company> getCompanyById(int companyId);
    boolean addCompany(String name);
}
