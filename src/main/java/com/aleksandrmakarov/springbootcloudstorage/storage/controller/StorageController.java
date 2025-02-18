package com.aleksandrmakarov.springbootcloudstorage.storage.controller;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.*;
import com.aleksandrmakarov.springbootcloudstorage.storage.service.StorageService;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import com.aleksandrmakarov.springbootcloudstorage.users.security.WebUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for handling requests related to storage operations
 * such as listing, renaming, deleting, and uploading objects.
 */
@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService; // Service layer handling business logic for storage operations.

    /**
     * Handles GET requests to display the storage index page, showing objects
     * in the specified prefix (if any) and generating breadcrumbs for navigation.
     *
     * @param prefix the prefix (folder path) to filter storage objects.
     * @param model  the model to pass data to the view.
     * @return the name of the view template for the storage index page.
     */
    @GetMapping
    public String index(@RequestParam(value = "prefix", required = false) String prefix,
                        Model model, @AuthenticationPrincipal WebUserDetails user) {
        List<StorageObjectModel> items = storageService.listObjects(user.getUsername(), prefix);
        List<Breadcrumb> breadcrumbs = StorageUtils.getBreadcrumbs(prefix);

        model.addAttribute("items", items);
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("user", user);

        return "storage/index";
    }

    /**
     * Handles GET requests to show the rename object page.
     *
     * @param prefix the prefix (folder path) of the object to rename.
     * @param name   the current name of the object.
     * @param model  the model to pass data to the view.
     * @return the name of the view template for renaming objects.
     */
    @GetMapping("rename")
    public String rename(@RequestParam("prefix") String prefix, @RequestParam("name") String name, Model model, @AuthenticationPrincipal WebUserDetails user) {
        model.addAttribute("prefix", prefix);
        model.addAttribute("name", name);
        model.addAttribute("user", user);
        return "storage/rename";
    }

    /**
     * Handles POST requests to rename a storage object.
     *
     * @param request       the rename request containing the prefix and new name.
     * @return a redirection to the storage index page.
     */
    @PostMapping("rename")
    public String rename(@ModelAttribute @Valid RenameStorageObjectRequest request, @AuthenticationPrincipal WebUserDetails user) {
        return buildStorageRedirectUrl(request.getPrefix());
    }

    /**
     * Handles GET requests to display the delete object confirmation page.
     *
     * @param prefix the prefix (folder path) of the object to delete.
     * @param name   the name of the object to delete.
     * @param model  the model to pass data to the view.
     * @return the name of the view template for deleting objects.
     */
    @GetMapping("delete")
    public String delete(@RequestParam(name = "prefix", required = false) String prefix, @RequestParam(name = "name") String name, Model model, @AuthenticationPrincipal WebUserDetails user) {
        model.addAttribute("prefix", prefix);
        model.addAttribute("name", name);
        model.addAttribute("user", user);
        return "storage/delete";
    }

    /**
     * Handles POST requests to delete a storage object.
     *
     * @param request       the delete request containing the prefix and name of the object to delete.
     * @param bindingResult result of validation checks on the request object.
     * @param model         the model to pass validation errors back to the view if necessary.
     * @return a redirection to the storage index page if successful, or the delete page if there are errors.
     */
    @PostMapping("delete")
    public String delete(@ModelAttribute @Valid DeleteStorageObjectRequest request, BindingResult bindingResult, Model model, @AuthenticationPrincipal WebUserDetails user) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("prefix", request.getPrefix());
            model.addAttribute("name", request.getName());
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));

            return "storage/delete";
        }

        storageService.deleteObject(user.getUsername(), request.getPrefix(), request.getName());

        return buildStorageRedirectUrl(request.getPrefix());
    }

    /**
     * Handles GET requests to display the upload page, including breadcrumbs for navigation.
     *
     * @param prefix the prefix (folder path) where the files will be uploaded.
     * @param model  the model to pass data to the view.
     * @return the name of the view template for uploading files.
     */
    @GetMapping("upload")
    public String upload(@RequestParam(value = "prefix", required = false) String prefix,
                         Model model, @AuthenticationPrincipal WebUserDetails user) {
        List<Breadcrumb> breadcrumbs = StorageUtils.getBreadcrumbs(prefix);
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("user", user);
        return "storage/upload";
    }

    /**
     * Handles POST requests to upload files to the specified prefix (folder path).
     *
     * @param request the upload request containing the prefix and files to upload.
     * @return a redirection to the storage index page.
     */
    @PostMapping("upload")
    public String upload(@ModelAttribute @Valid UploadFilesRequest request, @AuthenticationPrincipal WebUserDetails user) {
        storageService.saveObjects(user.getUsername(), request.getPrefix(), request.getFiles());

        return buildStorageRedirectUrl(request.getPrefix());
    }

    @GetMapping("create")
    public String create(@RequestParam(name = "prefix", required = false) String prefix, Model model, @AuthenticationPrincipal WebUserDetails user) {

        model.addAttribute("prefix", prefix);
        model.addAttribute("user", user);

        return "storage/create";
    }

    @PostMapping("create")
    public String create(@ModelAttribute @Valid CreateStorageObjectRequest request, BindingResult bindingResult, Model model, @AuthenticationPrincipal WebUserDetails user) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("prefix", request.getPrefix());
            model.addAttribute("name", request.getName());
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            return "storage/create";
        }

        storageService.createObject(user.getUsername(), request.getPrefix(), request.getName());

        return buildStorageRedirectUrl(request.getPrefix() + request.getName() + "/");
    }

    @GetMapping("search")
    public String search(@RequestParam("query") String query, Model model, @AuthenticationPrincipal WebUserDetails user) {

        List<SearchStorageObject> items = storageService.searchObjects(user.getUsername(), "", query);

        model.addAttribute("items", items);
        model.addAttribute("user", user);

        return "storage/search";
    }

    @GetMapping("download")
    public String download(@RequestParam("object") String object, @AuthenticationPrincipal WebUserDetails user) {
        return "redirect:" + storageService.downloadObject(user.getUsername(), "", object);
    }

    private String buildStorageRedirectUrl(String prefix) {

        String redirectUrl = "redirect:/storage";

        if (prefix != null && !prefix.isEmpty()) {
            redirectUrl += "?prefix=" + prefix;
        }

        return redirectUrl;
    }
}
