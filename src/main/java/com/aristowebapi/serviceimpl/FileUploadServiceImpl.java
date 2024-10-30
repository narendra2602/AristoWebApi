package com.aristowebapi.serviceimpl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.dao.IqviaRepository;
import com.aristowebapi.dao.IqviaUploadRepository;
import com.aristowebapi.dto.IqviaUploadRecordDto;
import com.aristowebapi.entity.Iqvia;
import com.aristowebapi.entity.IqviaUploadRecord;
import com.aristowebapi.request.FileUploadRequest;
import com.aristowebapi.response.FileUploadResponse;
import com.aristowebapi.service.FileUploadService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
@Service

public class FileUploadServiceImpl implements FileUploadService {

	@Autowired 
	private IqviaRepository iqviaRepository;
	
	@Autowired 
	private IqviaUploadRepository iqviaUploadRepository;

	private static final int BATCH_SIZE = 900;
	
	@Override
	public FileUploadResponse uploadFile(FileUploadRequest request) {
		
		FileUploadResponse fileUploadResponse= new FileUploadResponse();
		String filepath=request.getFilePath();
		String  filename=request.getFileName();
		String uploadtime=request.getUploadTime();
		
//		File file = new File(request.getFilePath()+"\\"+request.getFileName());
		
		File file = new File(request.getFilePath()+request.getFileName());
		
		IqviaUploadRecord iqviaUploadRecord=null;
		System.out.println("file name is "+file);
		
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
		
		
//		System.out.println(file.getAbsolutePath());
		//Reader reader = new InputStreamReader(fileReader.getInputStream());  

		  // Parse CSV data
		  CSVReader csvReader = new CSVReaderBuilder(fileReader).build();
		  List<String[]> rows = csvReader.readAll();
		  int size=rows.size();
		  
		  
		   iqviaUploadRecord= new IqviaUploadRecord();
		  iqviaUploadRecord.setUser_id(request.getLoginId());
		  iqviaUploadRecord.setFile_path(filepath);
		  iqviaUploadRecord.setFile_name(filename);
		  iqviaUploadRecord.setTotal_records(size);
		  iqviaUploadRecord = iqviaUploadRepository.save(iqviaUploadRecord);

		  Iqvia iqvia = null;
		  List<Iqvia> dataList = new ArrayList<Iqvia>();
		  long start = System.currentTimeMillis();
	        System.out.println("before Inserting in List .......... "+start);
/*		  for (int i = 6; i < rows.size(); i++) {
			  
			   String [] col = rows.get(i);
			   
			   
			   dataList.add(iqvia);
			   //iqviaRepository.save(iqvia);

			  
	            iqviaRepository.save(iqvia);
	            if (i % 50 == 0) { // Flush and clear the session every 50 inserts
	            	iqviaRepository.flush();
	            	iqviaRepository.clear();
	            }
	        }
*/	   
		  // Analyze data...

		//  System.out.println("Processed new method " + rows.size() + " rows! "+dataList.size());
		  int batchSize=900;
		  //int size = dataList.size();
		  
		  
		 
		  List<Iqvia> dataList1 = new ArrayList<>();

		  for (String[] col : rows) {
		      Iqvia iqvia1 = mapToIqvia(col);
		      dataList1.add(iqvia1);

		      if (dataList1.size() == batchSize) {
		          iqviaRepository.saveAll(dataList1);  // Batch insert
		          iqviaUploadRecord.setRecords_uploaded(iqviaUploadRecord.getRecords_uploaded()+batchSize);
		          iqviaUploadRecord = iqviaUploadRepository.save(iqviaUploadRecord);
		          dataList1.clear();  // Clear after batch save
		      }
		  }

		  // Save remaining records
		  if (!dataList1.isEmpty()) {
		      iqviaRepository.saveAll(dataList1);
	          iqviaUploadRecord.setRecords_uploaded(iqviaUploadRecord.getRecords_uploaded()+dataList1.size());
	          iqviaUploadRecord = iqviaUploadRepository.save(iqviaUploadRecord);
		  }

		  
		  
		  
		  
/*		  for (int j = 0; j < size; j += batchSize) {
	            if( j+ batchSize > size){
	                List<Iqvia> books1 = dataList.subList(j, size - 1);
	                iqviaRepository.saveAll(books1);
	                break;
	            }
	            List<Iqvia> books1 = dataList.subList(j, j + batchSize);
	            iqviaRepository.saveAll(books1);
	        }*/
	  
		  //System.out.println("Finished List inserting "+rows+" objects in :" + (System.currentTimeMillis() - start));

		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(filepath+" "+filename+" "+uploadtime);
		
		fileUploadResponse.setMessage("Success");
		fileUploadResponse.setNoOfRecords(iqviaUploadRecord.getTotal_records());
		fileUploadResponse.setNoOfRecordsProcessed(iqviaUploadRecord.getRecords_uploaded());
		return fileUploadResponse;
	}

	
	 
	
	  // Method to map CSV columns to Iqvia entity
    private Iqvia mapToIqvia(String[] col) {
        Iqvia iqvia = new Iqvia();
		   iqvia.setTag(col[0]);
		   iqvia.setBranch(col[1]);
		   iqvia.setMolecule(col[2]);
		   iqvia.setRank_mat1(col[3]);
		   iqvia.setRank_mat2(col[4]);
		   iqvia.setRank_mat3(col[5]);
		   iqvia.setRank_mat4(col[6]);
		   iqvia.setRank_mat5(col[7]);
		   iqvia.setProduct(col[8]);
		   iqvia.setCompay(col[9]);
		   iqvia.setDivision(col[10]);
		   iqvia.setLaunch(col[11]);
		   iqvia.setMat_val_1(col[12]);
		   iqvia.setMat_val_2(col[13]);
		   iqvia.setMat_val_3(col[14]);
		   iqvia.setMat_val_4(col[15]);
		   iqvia.setMat_val_5(col[16]);
		   iqvia.setMat_msper_1(col[17]);
		   iqvia.setMat_msper_2(col[18]);
		   iqvia.setMat_msper_3(col[19]);
		   iqvia.setMat_msper_4(col[20]);
		   iqvia.setMat_msper_5(col[21]);
		   iqvia.setMat_msglper_1(col[22]);
		   iqvia.setMat_msglper_2(col[23]);
		   iqvia.setMat_msglper_3(col[24]);
		   iqvia.setMat_msglper_4(col[25]);
		   iqvia.setMat_grtper_1(col[26]);
		   iqvia.setMat_grtper_2(col[27]);
		   iqvia.setMat_grtper_3(col[28]);
		   iqvia.setMat_grtper_4(col[29]);
		   
		   iqvia.setCy_cumm_val(col[30]);
		   iqvia.setCumm_grt_per(col[31]);
		   iqvia.setMat_unit_1(col[32]);
		   iqvia.setMat_unit_2(col[33]);
		   iqvia.setMat_unit_3(col[34]);
		   iqvia.setMat_unit_4(col[35]);
		   iqvia.setMat_unit_5(col[36]);
		   iqvia.setUmat_msper_1(col[37]);
		   iqvia.setUmat_msper_2(col[38]);
		   iqvia.setUmat_msper_3(col[39]);
		   iqvia.setUmat_msper_4(col[40]);
		   iqvia.setUmat_msper_5(col[41]);
		   iqvia.setUmat_msglper_1(col[42]);
		   iqvia.setUmat_msglper_2(col[43]);
		   iqvia.setUmat_msglper_3(col[44]);
		   iqvia.setUmat_msglper_4(col[45]);
		   iqvia.setUmat_grtper_1(col[46]);
		   iqvia.setUmat_grtper_2(col[47]);
		   iqvia.setUmat_grtper_3(col[48]);
		   iqvia.setUmat_grtper_4(col[49]);
		  
		   iqvia.setUcy_cumm_val(col[50]);
		   iqvia.setUcumm_grt_per(col[51]);

		   
		   
		   
		   iqvia.setQtrval_5(col[52]);
		   iqvia.setQtrval_6(col[53]);
		   iqvia.setQtrval_7(col[54]);
		   iqvia.setQtrval_8(col[55]);

		   iqvia.setQtr_msper_5(col[56]);
		   iqvia.setQtr_msper_6(col[57]);
		   iqvia.setQtr_msper_7(col[58]);
		   iqvia.setQtr_msper_8(col[59]);
		   iqvia.setQtr_msgl_1(col[60]);
		   iqvia.setQtr_msgl_2(col[61]);
		   iqvia.setQtr_msgl_3(col[62]);
		   iqvia.setQtr_msgl_4(col[63]);

		   iqvia.setQtr_grt_5(col[64]);
		   iqvia.setQtr_grt_6(col[65]);
		   iqvia.setQtr_grt_7(col[66]);
		   iqvia.setQtr_grt_8(col[67]);

		   iqvia.setQtr_unit_5(col[68]);
		   iqvia.setQtr_unit_6(col[69]);
		   iqvia.setQtr_unit_7(col[70]);
		   iqvia.setQtr_unit_8(col[71]);

		   iqvia.setQtr_unitms_5(col[72]);
		   iqvia.setQtr_unitms_6(col[73]);
		   iqvia.setQtr_unitms_7(col[74]);
		   iqvia.setQtr_unitms_8(col[75]);
		   iqvia.setQtr_unitmsgl_1(col[76]);
		   iqvia.setQtr_unitmsgl_2(col[77]);
		   iqvia.setQtr_unitmsgl_3(col[78]);
		   iqvia.setQtr_unitmsgl_4(col[79]);

		   iqvia.setQtr_unitmsgrt_5(col[80]);
		   iqvia.setQtr_unitmsgrt_6(col[81]);
		   iqvia.setQtr_unitmsgrt_7(col[82]);
		   iqvia.setQtr_unitmsgrt_8(col[83]);

		   iqvia.setMnth_val13(col[84]);
		   iqvia.setMnth_val14(col[85]);
		   iqvia.setMnth_val15(col[86]);
		   iqvia.setMnth_val16(col[87]);
		   iqvia.setMnth_val17(col[88]);
		   iqvia.setMnth_val18(col[89]);
		   iqvia.setMnth_val19(col[90]);
		   iqvia.setMnth_val20(col[91]);
		   iqvia.setMnth_val21(col[92]);
		   iqvia.setMnth_val22(col[93]);
		   iqvia.setMnth_val23(col[94]);
		   iqvia.setMnth_val24(col[95]);

		   iqvia.setMnth_valms_13(col[96]);
		   iqvia.setMnth_valms_14(col[97]);
		   iqvia.setMnth_valms_15(col[98]);
		   iqvia.setMnth_valms_16(col[99]);
		   iqvia.setMnth_valms_17(col[100]);
		   iqvia.setMnth_valms_18(col[101]);
		   iqvia.setMnth_valms_19(col[102]);
		   iqvia.setMnth_valms_20(col[103]);
		   iqvia.setMnth_valms_21(col[104]);
		   iqvia.setMnth_valms_22(col[105]);
		   iqvia.setMnth_valms_23(col[106]);
		   iqvia.setMnth_valms_24(col[107]);
		   iqvia.setMnth_valgl_1(col[108]);
		   iqvia.setMnth_valgl_2(col[109]);
		   iqvia.setMnth_valgl_3(col[110]);
		   iqvia.setMnth_valgl_4(col[111]);
		   iqvia.setMnth_valgl_5(col[112]);
		   iqvia.setMnth_valgl_6(col[113]);
		   iqvia.setMnth_valgl_7(col[114]);
		   iqvia.setMnth_valgl_8(col[115]);
		   iqvia.setMnth_valgl_9(col[116]);
		   iqvia.setMnth_valgl_10(col[117]);
		   iqvia.setMnth_valgl_11(col[118]);
		   iqvia.setMnth_valgl_12(col[119]);
		   iqvia.setMnth_valgrt_1(col[120]);
		   iqvia.setMnth_valgrt_2(col[121]);
		   iqvia.setMnth_valgrt_3(col[122]);
		   iqvia.setMnth_valgrt_4(col[123]);
		   iqvia.setMnth_valgrt_5(col[124]);
		   iqvia.setMnth_valgrt_6(col[125]);
		   iqvia.setMnth_valgrt_7(col[126]);
		   iqvia.setMnth_valgrt_8(col[127]);
		   iqvia.setMnth_valgrt_9(col[128]);
		   iqvia.setMnth_valgrt_10(col[129]);
		   iqvia.setMnth_valgrt_11(col[130]);
		   iqvia.setMnth_valgrt_12(col[131]);

		   iqvia.setMnth_unit13(col[132]);
		   iqvia.setMnth_unit14(col[133]);
		   iqvia.setMnth_unit15(col[134]);
		   iqvia.setMnth_unit16(col[135]);
		   iqvia.setMnth_unit17(col[136]);
		   iqvia.setMnth_unit18(col[137]);
		   iqvia.setMnth_unit19(col[138]);
		   iqvia.setMnth_unit20(col[139]);
		   

		   iqvia.setMnth_unit21(col[140]);
		   iqvia.setMnth_unit22(col[141]);
		   iqvia.setMnth_unit23(col[142]);
		   iqvia.setMnth_unit24(col[143]);

		   iqvia.setMnth_unitms_13(col[144]);
		   iqvia.setMnth_unitms_14(col[145]);
		   iqvia.setMnth_unitms_15(col[146]);
		   iqvia.setMnth_unitms_16(col[147]);
		   iqvia.setMnth_unitms_17(col[148]);
		   iqvia.setMnth_unitms_18(col[149]);
		   iqvia.setMnth_unitms_19(col[150]);
		   iqvia.setMnth_unitms_20(col[151]);
		   iqvia.setMnth_unitms_21(col[152]);
		   iqvia.setMnth_unitms_22(col[153]);
		   iqvia.setMnth_unitms_23(col[154]);
		   iqvia.setMnth_unitms_24(col[155]);
		   iqvia.setMnth_unitgl_1(col[156]);
		   iqvia.setMnth_unitgl_2(col[157]);
		   iqvia.setMnth_unitgl_3(col[158]);
		   iqvia.setMnth_unitgl_4(col[159]);
		   iqvia.setMnth_unitgl_5(col[160]);
		   iqvia.setMnth_unitgl_6(col[161]);
		   iqvia.setMnth_unitgl_7(col[162]);
		   iqvia.setMnth_unitgl_8(col[163]);
		   iqvia.setMnth_unitgl_9(col[164]);
		   iqvia.setMnth_unitgl_10(col[165]);
		   iqvia.setMnth_unitgl_11(col[166]);
		   iqvia.setMnth_unitgl_12(col[167]);
		   iqvia.setMnth_unitgrt_1(col[168]);
		   iqvia.setMnth_unitgrt_2(col[169]);
		   iqvia.setMnth_unitgrt_3(col[170]);
		   iqvia.setMnth_unitgrt_4(col[171]);
		   iqvia.setMnth_unitgrt_5(col[172]);
		   iqvia.setMnth_unitgrt_6(col[173]);
		   iqvia.setMnth_unitgrt_7(col[174]);
		   iqvia.setMnth_unitgrt_8(col[175]);
		   iqvia.setMnth_unitgrt_9(col[176]);
		   iqvia.setMnth_unitgrt_10(col[177]);
		   iqvia.setMnth_unitgrt_11(col[178]);
		   iqvia.setMnth_unitgrt_12(col[179]);
        // Add other fields as needed...
        return iqvia;
    }




	@Override
	public FileUploadResponse getIqviaUploadRecord(int login_id) {
		
		IqviaUploadRecordDto iqviaUploadRecord=iqviaUploadRepository.getIqviaUploadedRecords(login_id);
		FileUploadResponse fileUploadResponse= new FileUploadResponse();
		fileUploadResponse.setMessage("Processing...");
		fileUploadResponse.setNoOfRecords(iqviaUploadRecord.getTotal_records());
		fileUploadResponse.setNoOfRecordsProcessed(iqviaUploadRecord.getRecords_uploaded());
		return fileUploadResponse;
	}

	

	
	
	
	
}
