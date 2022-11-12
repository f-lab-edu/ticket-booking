package org.project.controller;

import org.project.configuration.OAuth2Properties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

  private final OAuth2Properties oAuth2Properties;

  FrontController(
      OAuth2Properties oAuth2Properties
  ) {
    this.oAuth2Properties = oAuth2Properties;
  }

  // inject googleClientId and googleRedirectUri into template.
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("googleClientId", this.oAuth2Properties.getGoogle().getClientId());
    model.addAttribute("googleRedirectUri", this.oAuth2Properties.getGoogle().getRedirectUri());
    return "login";
  }
}
