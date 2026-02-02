package com.aristowebapi.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.FieldSetupRepository;
import com.aristowebapi.dao.FieldSetupUploadRepository;
import com.aristowebapi.entity.FieldSetup;
import com.aristowebapi.entity.IqviaUploadRecord;
import com.aristowebapi.request.FileUploadRequest;
import com.aristowebapi.response.FileUploadResponse;
import com.aristowebapi.service.FieldSetupUploadService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
@Primary
public class FieldSetupUploadServiceImpl implements FieldSetupUploadService {

    @Autowired
    private FieldSetupRepository fieldSetupRepository;

    @Autowired
    private FieldSetupUploadRepository fieldSetupUploadRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 900;

    @Override
    @Transactional
    public FileUploadResponse uploadFile(FileUploadRequest request) {

        FileUploadResponse response = new FileUploadResponse();

        // ================= STEP 1: CLEAR TABLE =================
        fieldSetupRepository.deleteAllData();

        File file = Paths.get(
                request.getFilePath(),
                request.getFileName()
        ).toFile();

        // ================= STEP 2: SAVE UPLOAD AUDIT =================
        IqviaUploadRecord uploadRecord = new IqviaUploadRecord();
        uploadRecord.setUser_id(request.getLoginId());
        uploadRecord.setFile_path(request.getFilePath());
        uploadRecord.setFile_name(request.getFileName());
        uploadRecord.setRecords_uploaded(0);
        uploadRecord.setTotal_records(0);
        uploadRecord = fieldSetupUploadRepository.save(uploadRecord);

        List<FieldSetup> batchList = new ArrayList<>(BATCH_SIZE);
        int totalRecords = 0;

        // ================= STEP 3: READ & INSERT CSV =================
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8
                ))
                .withSkipLines(1) // skip header
                .build()) {

            String[] row;

            while ((row = reader.readNext()) != null) {

                // 🔴 MUST be 17 columns (0–16)
                if (row.length < 17) {
                    continue;
                }

                batchList.add(mapToFieldSetup(row));
                totalRecords++;

                if (batchList.size() == BATCH_SIZE) {
                    saveBatch(batchList, uploadRecord);
                }
            }

            if (!batchList.isEmpty()) {
                saveBatch(batchList, uploadRecord);
            }

            uploadRecord.setTotal_records(totalRecords);
            fieldSetupUploadRepository.save(uploadRecord);

            response.setMessage("SUCCESS");
            response.setNoOfRecords(totalRecords);
            response.setNoOfRecordsProcessed(uploadRecord.getRecords_uploaded());

        } catch (Exception e) {
            throw new RuntimeException("CSV Upload Failed", e);
        }

        // ================= STEP 4: CALL STORED PROCEDURE =================
        fieldSetupRepository.updateAllData();

        return response;
    }

    // ================= BATCH SAVE =================

    private void saveBatch(List<FieldSetup> batchList, IqviaUploadRecord uploadRecord) {

        fieldSetupRepository.saveAll(batchList);
        entityManager.flush();
        entityManager.clear();

        uploadRecord.setRecords_uploaded(
                uploadRecord.getRecords_uploaded() + batchList.size()
        );
        fieldSetupUploadRepository.save(uploadRecord);

        batchList.clear();
    }

    // ================= CSV → ENTITY MAPPING =================

    private FieldSetup mapToFieldSetup(String[] col) {

        FieldSetup f = new FieldSetup();

        f.setBu(col[0]);
        f.setLevel_3(col[1]);
        f.setLevel_2(col[2]);
        f.setLevel_1(col[3]);
        f.setLocation(col[4]);
        f.setLevel(col[5]);
        f.setUser_name(col[6]);
        f.setUser_code(col[7]);
        f.setLogin_id(parseInt(col[8]));
        f.setPassword(col[9]);
        f.setUser_role(col[10]);
        f.setDesignation(col[11]);
        f.setUser_type(col[12]);
        f.setParent_location(col[13]);
        f.setReporting_to(col[14]);
        f.setGeoseqno(parseInt(col[15]));
        f.setGeodtseqno(parseInt(col[16]));

        return f;
    }

    private int parseInt(String val) {
        try {
            return (val == null || val.trim().isEmpty())
                    ? 0
                    : Integer.parseInt(val.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
