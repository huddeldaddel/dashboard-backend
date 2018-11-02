package engineer.thomas_werner.dashboard.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        try(final Reader r = new InputStreamReader(HomeController.class.getResourceAsStream("home.html"));
            final BufferedReader br = new BufferedReader(r)) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("Failed to load home.html", e);
            return "Not good";
        }
    }

}
