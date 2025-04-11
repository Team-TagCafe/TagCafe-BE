package com.Minjin.TagCafe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {
    @GetMapping("/")
    public RedirectView redirectToFrontend() {
        return new RedirectView("https://tagcafe.site");
    }
}
