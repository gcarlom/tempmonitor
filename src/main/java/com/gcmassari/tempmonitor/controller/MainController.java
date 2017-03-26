package com.gcmassari.tempmonitor.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcmassari.tempmonitor.main.Constants;
import com.gcmassari.tempmonitor.model.EvaluationParams;
import com.gcmassari.tempmonitor.model.FileBucket;
import com.gcmassari.tempmonitor.model.TemperatureSample;
import com.gcmassari.tempmonitor.model.TemperatureSamples;
import com.gcmassari.tempmonitor.utils.Utility;
import com.gcmassari.tempmonitor.validator.EvalParametersValidator;

@Controller
public class MainController {

  @InitBinder("evaluationParams")
  // @InitBinder: change to InitBinder("<name-of-ModelAttribute>")
  public void formatDatesDataBinding(WebDataBinder binder) {
    // // binder.addValidators(userValidator, emailValidator);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @RequestMapping(value = {"/"}, method = RequestMethod.GET)
  public String getHomePage(Model m) throws UnsupportedEncodingException {
    // Constants
    m.addAttribute("buildVersion", Constants.BUILD_VERSION);

    // Add to the model a fileBucket as in home.jsp we have the form
    // modelAttribute "fileBucket"
    // if we didn't add this we would get:
    // java.lang.IllegalStateException: Neither BindingResult nor plain
    // target object for bean name 'fileBucket' available as request
    // attribute
    m.addAttribute("fileBucket", new FileBucket());
    return "home";
  }

  @Autowired
  private EvalParametersValidator evalParamsValidator;

  @ExceptionHandler(NumberFormatException.class)
  public String handleNumberFormatException(NumberFormatException eNumberFormatException) {
    return "numberFormat exception ";
    // add error for view here
  }

  @ExceptionHandler(BindException.class)
  public String handleBindException(BindException be) {
    // extract data from exception...
    return "whatever";
  }

  @RequestMapping(value = "/showResults", method = RequestMethod.POST)
  public String showEvalResults(@ModelAttribute("evaluationParams") EvaluationParams params,
      BindingResult result, RedirectAttributes redirectAttributes, Model m, HttpSession session) {

    m.addAttribute("evaluationParams", params);
    m.addAttribute("buildVersion", Constants.BUILD_VERSION);

    evalParamsValidator.validate(params, result);
    if (result.hasErrors()) {
      return "evaluate";
    }
    m.addAttribute("showTable", true);
    @SuppressWarnings("unchecked")
    // no other way to get attributes: suppress warning
    TemperatureSamples samples = (TemperatureSamples) session.getAttribute("samples");

    List<String> jsonList = new ArrayList<String>();

    // TODO GIM check if samples is empty
    for (TemperatureSample sample : samples.getSamples()) {
      jsonList.add(Utility.getSampleAsJsonString(sample, params));
    }
    String jsonStr =
        "{ " + "\"jsonSamples\": [ " + StringUtils.join(jsonList, " , ") + " ], " + "\"tempMin\": "
            + params.getMinTemp() + ", " + "\"tempMax\": " + params.getMaxTemp() + " " + "}";

    m.addAttribute("jsonData", jsonStr);

    // "samples":List<TemperatureSample>, "file":String already stored as
    // session attribute
    return "results";
  }

}
