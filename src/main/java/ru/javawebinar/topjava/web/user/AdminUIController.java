package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping("/admin/users")
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void create(@RequestParam String name,
                       @RequestParam String email,
                       @RequestParam String password) {
        super.create(new User(null, name, email, password, Role.USER));
    }

    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveEnable(@RequestParam boolean enable, @PathVariable int id) {
        User user = super.get(id);
        super.saveEnable(enable, user);
    }

/*    @GetMapping ("/{id}")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/with-meals"})
    public User getWithMeals() {
        return super.getWithMeals(authUserId());
    }*/
}
