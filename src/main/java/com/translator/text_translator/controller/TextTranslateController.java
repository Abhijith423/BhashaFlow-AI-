package com.translator.text_translator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class TextTranslateController {

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/translate-text")
    public String translateText(
            @RequestParam("email") String email,
            @RequestParam("targetLang") String targetLang,
            @RequestParam("sourceText") String sourceText,
            Model model) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("email", email);
            body.add("targetLang", targetLang);
            body.add("text", sourceText);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(n8nWebhookUrl, request, String.class);

            model.addAttribute("success", "Successfully sent to n8n: " + email);
        }
        catch (Exception e) {
            model.addAttribute("error", "Failed to send to n8n: " + e.getMessage());
        }

        return "index";
    }
}
