package com.record.controller;

import com.record.service.ReferenceValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReferenceValueController {

    @Autowired
    private ReferenceValueService referenceValueService;

    @GetMapping("/referenceValue")
    public Map<String, String> getReferenceValues(@RequestParam(name = "moduleName") String moduleName, @RequestParam(name = "category") String category) {
        return referenceValueService.getReferenceValue(moduleName, category);
    }
}
