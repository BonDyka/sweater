package loc.abondarev.sweater.controller;

import loc.abondarev.sweater.domain.Message;
import loc.abondarev.sweater.domain.User;
import loc.abondarev.sweater.domain.dto.MessageDto;
import loc.abondarev.sweater.repos.MessageRepo;
import loc.abondarev.sweater.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = ("id"), direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        Page<MessageDto> page = messageService.messageList(pageable, user, filter);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file")MultipartFile file,
            @PageableDefault(sort = ("id"), direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);
            messageRepo.save(message);
            model.addAttribute("message", null);
        }

        Page<MessageDto> page = messageService.messageList(pageable, user, null);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");

        return "main";
    }

    private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            final File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            final String uuidFile = UUID.randomUUID().toString();
            final String resultFilename = String.format("%s%s", uuidFile, file.getOriginalFilename());

            file.transferTo(new File(String.format("%s/%s", uploadPath, resultFilename)));

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = ("id"), direction = Sort.Direction.DESC) Pageable pageable
    ) {
        final Set<Message> messages = author.getMessages();

        final Page<MessageDto> page = messageService.messageListForUser(author, currentUser, pageable);

        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubsciptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/user-messages/" + author.getId());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            saveFile(message, file);

            messageRepo.save(message);
        }
        return "redirect:/user-messages/" + user;
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User user,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        final Set<User> likes = message.getLikes();

        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }

        final UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .forEach((k, v) -> redirectAttributes.addAttribute(k, v));

        return "redirect:" + components.getPath();
    }
}