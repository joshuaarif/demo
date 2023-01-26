package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class JobController {

//	2. Get job list API
//	The API should make http request to
//	http://dev3.dansmultipro.co.id/api/recruitment/positions.json and return jobs data as
//	response payload.
	@GetMapping(value = "/positions")
	public List<Object> getPositions() {
		String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
		RestTemplate restTemplate = new RestTemplate();
		Object[] positions = restTemplate.getForObject(url, Object[].class);
		return Arrays.asList(positions);
	}

//	3. Get job detail API
//	The API should make http request
//	http://dev3.dansmultipro.co.id/api/recruitment/positions/{ID} and return job detail
//	data as response payload.
	@GetMapping("/positionsById")
	public Object getPositionsById(@RequestParam String id) {
		String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions/" + id;
		RestTemplate restTemplate = new RestTemplate();
		Object positions = restTemplate.getForObject(url, Object.class);
		return positions;
	}

//	4. Download job list API
//	The API should make http request
//	http://dev3.dansmultipro.co.id/api/recruitment/positions.json and return job list data
//	as CSV file.
	@GetMapping(value = "/positionsExportCSV", produces = "text/csv")
	public ResponseEntity<Resource> exportCSV() {

		List<List<String>> csvBody = new ArrayList<>();

		String url = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
		RestTemplate restTemplate = new RestTemplate();
		Object[] positions = restTemplate.getForObject(url, Object[].class);
		List<Object> objects = Arrays.asList(positions);

		ArrayList<String> arrayListPosition = new ArrayList<String>();

		for (Object object : objects) {
			arrayListPosition.add(object.toString());
		}

		csvBody.add(arrayListPosition);

		ByteArrayInputStream byteArrayOutputStream;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);) {
			for (List<String> record : csvBody)
				csvPrinter.printRecord(record);

			csvPrinter.flush();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

		String csvFileName = "people.csv";

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
		headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

		return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
	}
}
