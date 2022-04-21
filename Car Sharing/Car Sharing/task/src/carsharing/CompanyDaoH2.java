package carsharing;

import java.util.List;
import java.util.Optional;

public class CompanyDaoH2 implements CompanyDao {
    @Override
    public Optional<List<Company>> getAllCompanies() {
        return H2Utils.getObjectList(
                "SELECT * FROM COMPANY;",
                rs -> new Company(rs.getInt("ID"), rs.getString("NAME"))
        );
    }

    @Override
    public Optional<Company> getCompanyById(int companyId) {
        var list = H2Utils.getObjectList(
                "SELECT * FROM COMPANY WHERE ID="+companyId+" LIMIT 1;",
                rs -> new Company(rs.getInt("ID"), rs.getString("NAME"))
        );
        if(list.isEmpty()) return Optional.empty();
        else return Optional.of(list.get().get(0));
    }

    @Override
    public boolean addCompany(String name) {
        try {
            H2Utils.execute("INSERT INTO COMPANY (name) VALUES ('" +name+"');");
        } catch(RuntimeException e) {
            return false;
        }
        return true;
    }
}
