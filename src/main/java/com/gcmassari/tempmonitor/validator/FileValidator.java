package com.gcmassari.tempmonitor.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.gcmassari.tempmonitor.main.Constants;
import com.gcmassari.tempmonitor.model.FileBucket;

@Component
public class FileValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return FileBucket.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		 
		if (target == null) {
			errors.rejectValue("file", "Error: target is null");
		}

		FileBucket bucket = (FileBucket) target;
		
        MultipartFile mpFile = bucket.getFile();
        if (mpFile == null) {
        	// Signature: 	rejectValue(String field, String errorCode, String defaultMessage)
        	errors.rejectValue("file", "multipart-file-null", "Error: multipart file is null");
        }
        
        if (mpFile.isEmpty()){
            errors.rejectValue("file", "file.empty", "File is empty");
        }
        
        if (mpFile.getSize() >= Constants.MAX_UPLOAD_FILE_SIZE  ){
        	errors.rejectValue("file", "file-too-big", "File is too big");
        }
        
        if (!mpFile.getOriginalFilename().endsWith(".csv")) {
        	errors.rejectValue("file", "invalid-file-format", "Invalid file format");
        }
	}

}
