package com.aleksandrmakarov.springbootcloudstorage.storage.controller;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.*;
import com.aleksandrmakarov.springbootcloudstorage.storage.service.StorageService;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public String index(@RequestParam(value = "prefix", required = false) String prefix,
                        Model model) {

        List<StorageObjectModel> items = storageService.findObjects(prefix);

        List<Breadcrumb> breadcrumbs = StorageUtils.getBreadcrumbs(prefix);

        model.addAttribute("items", items);
        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storage/index";
    }

    @GetMapping("rename")
    public String rename(@RequestParam("prefix") String prefix, @RequestParam("name") String name, Model model) {

        model.addAttribute("prefix", prefix);
        model.addAttribute("name", name);

        return "storage/rename";
    }

    @PostMapping("rename")
    public String rename(@ModelAttribute @Valid RenameStorageObjectRequest request, BindingResult bindingResult) {

        String redirectUrl = "redirect:/storage";

        if (request.getPrefix() != null && !request.getPrefix().isEmpty()) {
            redirectUrl += "?prefix=" + request.getPrefix();
        }

        return redirectUrl;
    }

    @GetMapping("delete")
    public String delete(@RequestParam(name = "prefix", required = false) String prefix, @RequestParam(name = "name") String name, Model model) {

        model.addAttribute("prefix", prefix);
        model.addAttribute("name", name);

        return "storage/delete";
    }

    @PostMapping("delete")
    public String delete(@ModelAttribute @Valid DeleteStorageObjectRequest request, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("prefix", request.getPrefix());
            model.addAttribute("name", request.getName());
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));

            return "storage/delete";
        }

        String redirectUrl = "redirect:/storage";

        if (request.getPrefix() != null && !request.getPrefix().isEmpty()) {
            redirectUrl += "?prefix=" + request.getPrefix();
        }

        return redirectUrl;
    }

    @GetMapping("upload")
    public String upload(@RequestParam(value = "prefix", required = false) String prefix,
                         Model model) {

        List<Breadcrumb> breadcrumbs = StorageUtils.getBreadcrumbs(prefix);

        model.addAttribute("breadcrumbs", breadcrumbs);

        return "storage/upload";
    }

    @PostMapping("upload")
    public String upload(@ModelAttribute @Valid UploadFilesRequest request) {

        storageService.saveObjects(request.getPrefix(), request.getFiles());

        String redirectUrl = "redirect:/storage";

        if (request.getPrefix() != null && !request.getPrefix().isEmpty()) {
            redirectUrl += "?prefix=" + request.getPrefix();
        }

        return redirectUrl;
    }
}
