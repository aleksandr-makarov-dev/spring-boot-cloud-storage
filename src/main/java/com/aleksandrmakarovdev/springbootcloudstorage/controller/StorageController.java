package com.aleksandrmakarovdev.springbootcloudstorage.controller;

import com.aleksandrmakarovdev.springbootcloudstorage.model.CreateObjectRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RemoveObjectRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.UploadObjectRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @GetMapping("upload")
    public String upload(Model model) {
        return "storage/upload";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam(value = "prefix", required = false) String prefix,
            @ModelAttribute("request") @Valid UploadObjectRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            return "storage/upload?prefix=" + prefix;
        }

        storageService.saveObjects(request.getFiles(), prefix);

        return "redirect:/?prefix=" + prefix;
    }

    @PostMapping("delete")
    public String delete(@RequestParam(value = "prefix") String prefix, @ModelAttribute("request") @Valid RemoveObjectRequest request) {

        // TODO: add functionality to delete folders
        storageService.removeObject(request.getObject());

        return "redirect:/?prefix=" + prefix;
    }

    @GetMapping("create")
    public String create() {
        return "storage/create";
    }

    @PostMapping("create")
    public String create(@RequestParam(value = "prefix") String prefix, @ModelAttribute("request") CreateObjectRequest request, BindingResult bindingResult) {

        storageService.createObject(prefix, request.getName());

        return "redirect:/?prefix=" + prefix;
    }
}
