package com.aleksandrmakarovdev.springbootcloudstorage.controller;

import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final StorageService storageService;

    @GetMapping
    public String home(@RequestParam(value = "path", required = false) String path, Model model) {

        List<StorageObject> items = storageService.findByPrefix(path);
        model.addAttribute("items", items);

        return "home";
    }
}
