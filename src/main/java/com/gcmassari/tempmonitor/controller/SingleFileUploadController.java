package com.gcmassari.tempmonitor.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcmassari.tempmonitor.main.Constants;
import com.gcmassari.tempmonitor.model.EvaluationParams;
import com.gcmassari.tempmonitor.model.FileBucket;
import com.gcmassari.tempmonitor.model.TemperatureSamples;
import com.gcmassari.tempmonitor.reader.CsvReader;
import com.gcmassari.tempmonitor.utils.Utility;
import com.gcmassari.tempmonitor.validator.FileValidator;

@Controller
public class SingleFileUploadController {

	
	@Autowired
	private FileValidator fileValidator;
	
	@InitBinder("fileBucket")
	public void registerFileValidatorIntoDataBinder(WebDataBinder binder) {
		binder.setValidator(fileValidator);
	}
	
	// Init Binder needed to have dates (Ignore before / after) in the form dd.MM.YYYY HH:mm:ss (eg 14.12.2016 11:15:24)
	//  If this doesn't occur then response to clicking on the "Evaluate" btn is a Error 400 Bad request
	// Because Spring MVC doesn't understand the STrings "Wed 14 Dec.." as Date therefore the Date's in
	// the EvaluationParams cannot be set -> it is as if you didn't sent an EvaluationParams attached to request /showResults
	//  -> leads to "400 Bad Request Error"
	@InitBinder("evaluationParams") 
	public void registerCustomDateEditorIntoDataBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
    // TODO GIM work on the new method below
    @RequestMapping(value = "/uploadSamplesSingleFile", method = RequestMethod.POST)
    public String uploadSamplesSingleFile(
    		@Valid FileBucket fileBucket, 
    		BindingResult result, 
    		RedirectAttributes redirectAttributes, 
    		Model m,
    		HttpSession session) throws IOException {
    	if (result.hasErrors()) {
    		//redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
    		// TODO remove temp file /dir + manage error display
    		return "home";
    	}

    	boolean isFileOk = false;
    	MultipartFile file = fileBucket.getFile();
    	
        Path path = null;
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();

            path = Paths.get( Constants.UPLOADED_FOLDER + File.separatorChar + file.getOriginalFilename() + "_" + new Date().getTime());
            Files.write(path, bytes);
            
            isFileOk = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // TODO GIM Add check on validity of file content (use exception or a more complex result type)
        TemperatureSamples temps = CsvReader.readTemperaturesFromFile(path.toString());
        
        if (!isFileOk || temps == null || temps.isEmpty()) {
        	// TODO remove temp file /dir + manage errors
        	return "error";
        }
        EvaluationParams parameters = Utility.getDefaultEvaluationParams(temps);
        //TODO GIM. store as session attribute (not in model)
        // m.addAttribute("samples", temps);
        // TODO GIM: check if this works
        session.setAttribute("samples", temps);
        session.setAttribute("file", file.getOriginalFilename().toString());
        m.addAttribute("evaluationParams", parameters);
        m.addAttribute("buildVersion", Constants.BUILD_VERSION);
//        m.addAttribute("fileBucket", new FileBucket());
        // TODO remove temp file /dir
        return "evaluate";
    }
}
