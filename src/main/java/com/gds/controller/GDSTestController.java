package com.gds.controller;

import com.gds.security.AESEncryption;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class GDSTestController {

    @Autowired
    private AESEncryption aesEncryption;

    @GetMapping(value = "/auth")
    public ResponseEntity<String> helloWorld(@RequestParam("text") String key) {
        if (null != key && !"".equals(key)) {
            String encryptedText = aesEncryption.encrypt(key,"E23847DIE23847DI");
            String decryptedText = aesEncryption.decrypt(encryptedText, "E23847DIE23847DI");
            String encryptDecrypt = "<br/>" + encryptedText + "<br/><br/>" +decryptedText;
            return new ResponseEntity<>("Encrypted and Decrypted Text :: "+encryptDecrypt, HttpStatus.OK);
        }
        return new ResponseEntity<>("Hello World !!!!", HttpStatus.OK);
    }

    @GetMapping(value = "/config")
    public ResponseEntity<String> waterMeter(@RequestBody Map<String, Object> key) {
        JSONObject json = new JSONObject();
        try {
            if (null != key && key.size() > 0) {
                String secretKey = key.get("key").toString();
                String encryptedText = aesEncryption.encrypt(key.get("body").toString(), secretKey);
                String decryptedText = aesEncryption.decrypt(key.get("body").toString(), secretKey);
                String encryptDecrypt = "<br/>" + encryptedText + "<br/><br/>" + decryptedText;
                json.put("message", "success");
                json.put("body", decryptedText);
                return new ResponseEntity<>(json.toJSONString(), HttpStatus.OK);
            }
            json.put("message", "failed");
            return new ResponseEntity<>(json.toJSONString(), HttpStatus.NO_CONTENT);
        }catch(Exception e) {
            json.put("messge", e.getMessage());
        }
        return new ResponseEntity<>(json.toJSONString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
