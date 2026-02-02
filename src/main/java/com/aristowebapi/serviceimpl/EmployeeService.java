package com.aristowebapi.serviceimpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.EmployeeRepository;
import com.aristowebapi.entity.Employee;
import com.aristowebapi.request.EmployeeUploadRequest;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // 🔥 DELETE OLD DATA + INSERT NEW DATA (SAFE)
    @Transactional
    public void saveCSVFromJson(EmployeeUploadRequest request) {

        try {
            // 1️⃣ Decode Base64 CSV
            byte[] csvBytes = Base64.getDecoder()
                    .decode(request.getFileData());

            InputStream is = new ByteArrayInputStream(csvBytes);

            try (Reader reader = new BufferedReader(
                    new InputStreamReader(is))) {

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

                // ❗ OPTIONAL SAFETY CHECK
                if (employees.isEmpty()) {
                    throw new RuntimeException("CSV file is empty");
                }

                // 2️⃣ DELETE OLD DATA
                repository.deleteAll();

                // 3️⃣ INSERT NEW DATA
                repository.saveAll(employees);
            }

        } catch (Exception e) {
            // ❗ Transaction will rollback automatically
            throw new RuntimeException("CSV JSON upload failed", e);
        }
    }
}
