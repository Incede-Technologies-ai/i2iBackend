// package com.I2I.I2IBaceknd.Controller;

// import java.io.IOException;
// import java.util.List;
// import java.util.Map;

// import org.json.simple.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.I2I.I2IBaceknd.Commonfn.commonfn;
// import com.I2I.I2IBaceknd.Service.Eligiblityinq_Service;




// @RestController
// public class Eligiblityinq_Controller {
    
//     @Autowired
//     private Eligiblityinq_Service inq_service;


//     @GetMapping({"api/v0/getInquiry", "api/v1/getInquiry"})
//     private List<Map<String, Object>> getInquiry() {
//         return inq_service.getInquiryList();
//     }
    

//     @PostMapping(path={"api/v0/save_inqury","api/v1/save_inqury"}, consumes = "MediaType.MULTIPART_FORM_DATA_VALUE")
//     public Map<String,Object>save_inqury(
//         @RequestBody String inquiry_details,
//         @RequestPart(value = "eds_2022", required = false) MultipartFile eds1,
//         @RequestPart(value = "eds_2023", required = false) MultipartFile eds2,
//         @RequestPart(value = "eds_2024", required = false) MultipartFile eds3,
//         @RequestPart(value = "eds_2025", required = false) MultipartFile eds4) throws IOException{
//         JSONObject obj = commonfn.createJSONObject(inquiry_details);
//         return inq_service.save_inqury(obj,eds1,eds2,eds3,eds4);
//     }
    


// }



package com.I2I.I2IBaceknd.Controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.I2I.I2IBaceknd.Commonfn.commonfn;
import com.I2I.I2IBaceknd.Service.Eligiblityinq_Service;

@RestController
public class Eligiblityinq_Controller {
    
    @Autowired
    private Eligiblityinq_Service inq_service;

    @GetMapping({"api/v0/getInquiry", "api/v1/getInquiry"})
    private List<Map<String, Object>> getInquiry() {
        return inq_service.getInquiryList();
    }
    
    @PostMapping(path = {"api/v0/save_inqury", "api/v1/save_inqury"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> save_inqury(
        @RequestParam String inquiry_details,
        @RequestPart(value = "eds_2022", required = false) MultipartFile eds1,
        @RequestPart(value = "eds_2023", required = false) MultipartFile eds2,
        @RequestPart(value = "eds_2024", required = false) MultipartFile eds3,
        @RequestPart(value = "eds_2025", required = false) MultipartFile eds4) throws IOException {
        JSONObject obj = commonfn.createJSONObject(inquiry_details);
        return inq_service.save_inqury(obj, eds1, eds2, eds3, eds4);
    }


   
 @GetMapping("/api/v1/get_documents")
public ResponseEntity<byte[]> getDocument(@RequestParam Integer id, @RequestParam String docType) {
    List<Map<String, Object>> docs = inq_service.getDocuments(id);

    if (docs == null || docs.isEmpty() || !docs.get(0).containsKey(docType)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Get the document as a byte array
    Object docObject = docs.get(0).get(docType);
    if (!(docObject instanceof byte[])) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    byte[] documentBytes = (byte[]) docObject;

    // Set response headers for file download
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // Generic file type
    headers.setContentDisposition(ContentDisposition.attachment()
            .filename(docType + ".pdf")  // Change the file extension if needed
            .build());

    return ResponseEntity.ok()
            .headers(headers)
            .body(documentBytes);
}



}
