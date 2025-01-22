package com.aleksandrmakarov.springbootcloudstorage.storage.controller;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import com.aleksandrmakarov.springbootcloudstorage.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public String index(@RequestParam(value = "prefix", required = false) String prefix,
                        Model model) {

        List<StorageObjectModel> items = storageService.findAll(prefix);

        model.addAttribute("items", items);

        return "storage/index";
    }

    @GetMapping("rename")
    public String rename(@RequestParam("prefix") String prefix, @RequestParam("name") String name, Model model) {

        model.addAttribute("prefix", prefix);
        model.addAttribute("name", name);

        return "storage/rename";
    }

    @GetMapping("delete")
    public String delete(@RequestParam("prefix") String prefix, @RequestParam("name") String name, Model model) {
        return "storage/delete";
    }

    @PostMapping("delete")
    public String delete(){

        return "redirect:/storage";
    }
}
