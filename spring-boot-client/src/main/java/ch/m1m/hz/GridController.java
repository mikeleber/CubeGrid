package ch.m1m.hz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GridController {

    @GetMapping("/")
    public String showUserListIndex(Model model) {
        model.addAttribute("user", "totoUser");
        model.addAttribute("myuser", "MikeMagic");
        return "index";
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("user", "totoUser");
        model.addAttribute("myuser", "MikeMagic");
        return "index";
    }
}
