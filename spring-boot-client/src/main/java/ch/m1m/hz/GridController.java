package ch.m1m.hz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GridController {

    @Value("${server.port}")
    String portAsString;

    @GetMapping("/")
    public String showUserListIndex(Model model) {
        return modelIndex(model);
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        return modelIndex(model);
    }

    private String modelIndex(Model model) {

        model.addAttribute("headerBackground", getHeaderBackground());

        model.addAttribute("user", "totoUser");
        model.addAttribute("myuser", "MikeMagic");
        return "index";
    }

    private String getHeaderBackground() {
        String valueHeaderBackground = "blue";
        if ("8095".equals(portAsString)) {
            valueHeaderBackground = "green";
        }
        return valueHeaderBackground;
    }
}
