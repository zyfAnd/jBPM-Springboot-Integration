package com.gclfax.jbpm.demo.controller;

import net.sf.json.JSONArray;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.kie.internal.query.QueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@RestController
@RequestMapping("/processinstance")
public class ProcessInstanceController {

    @Autowired
    private RuntimeDataService runtimeDataService;

    @Autowired
    private ProcessService processService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getProcessInstances() {

        Collection<ProcessInstanceDesc> processInstances = runtimeDataService.getProcessInstances(new QueryContext(0, 20, "id", false));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ProcessinstanceList");
        modelAndView.addObject("ProcessinstanceListJson", JSONArray.fromObject(processInstances).toString());
        modelAndView.addObject("processInstances", processInstances);
        return modelAndView;

    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ProcessInstanceDesc getProcessInstance(@RequestParam String id) {
        long processInstanceId = Long.parseLong(id);
        ProcessInstanceDesc processInstance = runtimeDataService.getProcessInstanceById(processInstanceId);

        return processInstance;
    }

    @RequestMapping(value = "/abort", method = RequestMethod.POST)
    public String abortProcessInstance(@RequestParam String id) {

        processService.abortProcessInstance(Long.parseLong(id));

        return "Instance (" + id + ") aborted successfully";
    }

    @RequestMapping(value = "/signal", method = RequestMethod.POST)
    public String signalProcessInstance(@RequestParam String id, @RequestParam String signal,
                                        @RequestParam String data) {

        processService.signalProcessInstance(Long.parseLong(id), signal, data);

        return "Signal sent to instance (" + id + ") successfully";
    }
}
