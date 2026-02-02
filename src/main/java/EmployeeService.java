import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aristowebapi.dao.EmployeeRepository;
import com.aristowebapi.entity.Employee;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void saveCSV(MultipartFile file) {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .build();

            List<Employee> employees = new ArrayList<>();

            for (String[] row : csvReader.readAll()) {
                Employee emp = new Employee();
                emp.setName(row[0]);
                emp.setEmail(row[1]);
                emp.setDepartment(row[2]);
                emp.setSalary(
                    row.length > 3 && !row[3].isEmpty()
                        ? Double.parseDouble(row[3])
                        : 0.0
                );
                employees.add(emp);
            }

            repository.saveAll(employees);

        } catch (Exception e) {
            throw new RuntimeException("CSV upload failed", e);
        }
    }
}
