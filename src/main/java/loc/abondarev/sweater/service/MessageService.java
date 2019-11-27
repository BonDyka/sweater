package loc.abondarev.sweater.service;

import loc.abondarev.sweater.domain.Message;
import loc.abondarev.sweater.domain.User;
import loc.abondarev.sweater.domain.dto.MessageDto;
import loc.abondarev.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    public Page<MessageDto> messageList(Pageable pageable, User user, String filter) {
        Page<MessageDto> result;

        if (filter != null && !filter.isEmpty()) {
            result = messageRepo.findByTag(filter, user, pageable);
        } else {
            result = messageRepo.findAll(pageable, user);
        }

        return result;
    }

    public Page<MessageDto> messageListForUser(User author, User currentUser, Pageable pageable) {
        return messageRepo.findByUser(author, currentUser, pageable);
    }
}
