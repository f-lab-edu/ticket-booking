package org.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

  private final String googleClientId;
  private final String googleRedirectUri;

  FrontController(
      @Value("${oauth2.google.client-id}")
      String googleClientId,
      @Value("${oauth2.google.redirect-uri}")
      String googleRedirectUri
  ) {
    this.googleClientId = googleClientId;
    this.googleRedirectUri = googleRedirectUri;
  }

  // inject googleClientId and googleRedirectUri into template.
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("googleClientId", googleClientId);
    model.addAttribute("googleRedirectUri", googleRedirectUri);
    return "login";
  }
}
