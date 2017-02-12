package com.gcmassari.tempmonitor.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcmassari.tempmonitor.main.Constants;
import com.gcmassari.tempmonitor.model.EvaluationParams;
import com.gcmassari.tempmonitor.model.FileBucket;
import com.gcmassari.tempmonitor.model.TemperatureSample;
import com.gcmassari.tempmonitor.model.TemperatureSamples;
import com.gcmassari.tempmonitor.reader.CsvReader;
import com.gcmassari.tempmonitor.utils.Utility;

@Controller
public class MainController {
	
	@InitBinder("evaluationParams")  // @InitBinder: change to  InitBinder("<name-of-ModelAttribute>")
	public void formatDatesDataBinding(WebDataBinder binder) {
//			//		binder.addValidators(userValidator, emailValidator);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
		public String getHomePage(Model m) throws UnsupportedEncodingException {
		// Constants 
		m.addAttribute("buildVersion", Constants.BUILD_VERSION);
		
		// Add to the model a fileBucket as in home.jsp we have the form modelAttribute "fileBucket"
		// if we didn't add this we would get:
		//  java.lang.IllegalStateException: Neither BindingResult nor plain target object for bean name 'fileBucket' available as request attribute
		m.addAttribute("fileBucket", new FileBucket());    
		return "home";
	}

	
    @RequestMapping(value = "/uploadSamples", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, 
    	Model m,  HttpSession session) throws IOException {
   
    	boolean isFileOk = false;
    	if (file.getName().contains("wrong")) {
//    		bindingResult.reject("wrong file");
    		redirectAttributes.addFlashAttribute("file", "Wrong file");
    		return "redirect:home";
    	}
    	
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            // TODO remove temp file /dir + manage error display
            return "error";
        }
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
        // TODO remove temp file /dir
        return "evaluate";
    }
    
    
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String missingParamterHandler(Exception exception) {
      /*inspect and exception and obtain meaningful message*/
    	System.out.println("--> **** missing params");
      return "default-error-view"; /*view name of your erro jsp*/
    }
    
	@RequestMapping(value = "/showResults", method = RequestMethod.POST)
	public String showEvalResults(@ModelAttribute("evaluationParams") EvaluationParams params, RedirectAttributes redirectAttributes, Model m, HttpSession session)  {
		m.addAttribute("evaluationParams", params);
		m.addAttribute("buildVersion", Constants.BUILD_VERSION);
		m.addAttribute("showTable", true);
		@SuppressWarnings("unchecked") // no other way to get attributes: supress warning
		TemperatureSamples samples = (TemperatureSamples) session.getAttribute("samples");
		
		List<String> jsonList = new ArrayList<String>();
		
		// TODO GIM check case samples is empty
		for (TemperatureSample sample : samples.getSamples()) {
			jsonList.add(Utility.getSampleAsJsonString(sample, params));
		}
		String jsonStr = "{ "
				+ "\"jsonSamples\": [ " + StringUtils.join(jsonList, " , ") + " ], "
				+ "\"tempMin\": " + params.getMinTemp() +", "
				+ "\"tempMax\": " + params.getMaxTemp() + " "
				+ "}";
		
		m.addAttribute("jsonData", jsonStr);
		
		// "samples":List<TemperatureSample>, "file":String already stored as session attribute
		return "results";
	}
	
}
