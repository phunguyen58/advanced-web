package com.ptudw.web.web.websocket;

import com.ptudw.web.domain.Assignment;
import com.ptudw.web.domain.Authority;
import com.ptudw.web.domain.GradeReview;
import com.ptudw.web.domain.Notification;
import com.ptudw.web.domain.User;
import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.repository.NotificationRepository;
import com.ptudw.web.repository.UserCourseRepository;
import com.ptudw.web.repository.UserRepository;
import com.ptudw.web.security.AuthoritiesConstants;
import com.ptudw.web.service.AssignmentService;
import com.ptudw.web.service.GradeReviewService;
import com.ptudw.web.service.NotificationService;
import com.ptudw.web.service.UserCourseService;
import com.ptudw.web.web.websocket.dto.NotificationDTO;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.mapping.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class PushNotificationService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final AssignmentService assignmentService;

    private final UserCourseService userCourseService;

    private final UserRepository userRepository;

    private final GradeReviewService gradeReviewService;

    private final NotificationService notificationService;

    private final UserCourseRepository userCourseRepository;

    private final NotificationRepository notificationRepository;

    public PushNotificationService(
        SimpMessageSendingOperations messagingTemplate,
        AssignmentService assignmentService,
        UserCourseService userCourseService,
        UserRepository userRepository,
        GradeReviewService gradeReviewService,
        NotificationService notificationService,
        UserCourseRepository userCourseRepository,
        NotificationRepository notificationRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.assignmentService = assignmentService;
        this.userCourseService = userCourseService;
        this.userRepository = userRepository;
        this.gradeReviewService = gradeReviewService;
        this.notificationService = notificationService;
        this.userCourseRepository = userCourseRepository;
        this.notificationRepository = notificationRepository;
    }

    @MessageMapping("/notification-student")
    public NotificationDTO sendNotificationStudent(
        @Payload NotificationDTO notificationDTO,
        StompHeaderAccessor stompHeaderAccessor,
        Principal principal
    ) {
        log.debug("Sending notification to student {}", notificationDTO);

        userRepository
            .findOneByStudentId(notificationDTO.getUserReceiverLogin())
            .ifPresent(u -> {
                Notification notification = new Notification();
                notification.setMessage(notificationDTO.getMessage());
                notification.setTopic(notificationDTO.getTopic());
                notification.setReceivers(u.getLogin());
                notification.setSender(principal.getName());
                notification.setLink("/grade-review/" + notificationDTO.getGradeReviewId() + "/edit");
                notification.setIsRead(false);
                notificationRepository.save(notification);
            });

        return notificationDTO;
    }

    @MessageMapping("/notification-teacher")
    public NotificationDTO sendNotificationTeacher(
        @Payload NotificationDTO notificationDTO,
        StompHeaderAccessor stompHeaderAccessor,
        Principal principal
    ) {
        log.debug("Sending notification to teacher {}", notificationDTO);

        gradeReviewService
            .findOne(Long.valueOf(notificationDTO.getGradeReviewId()))
            .ifPresent(gradeReview -> {
                assignmentService
                    .findOne(gradeReview.getAssigmentId())
                    .ifPresent(assignment -> {
                        List<UserCourse> userCourses = userCourseRepository.findAllByCourseId(assignment.getCourse().getId());
                        List<Long> userIds = Optional
                            .ofNullable(userCourses)
                            .orElse(Collections.emptyList())
                            .stream()
                            .filter(Objects::nonNull)
                            .map(UserCourse::getUserId)
                            .collect(Collectors.toList());
                        List<User> users = userRepository.findAllWithAuthoritiesByIdIn(userIds);

                        // Authority teacherAuthority = new Authority();
                        // teacherAuthority.setName(AuthoritiesConstants.TEACHER);

                        List<String> receivers = new ArrayList<>();
                        List<Notification> notifications = new ArrayList<>();
                        users
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(user ->
                                user
                                    .getAuthorities()
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .map(Authority::getName)
                                    .collect(Collectors.toList())
                                    .contains(AuthoritiesConstants.TEACHER)
                            )
                            .forEach(u -> {
                                Notification notification = new Notification();
                                notification.setMessage(notificationDTO.getMessage());
                                notification.setTopic(notificationDTO.getTopic());
                                notification.setReceivers(u.getLogin());
                                notification.setSender(principal.getName());
                                notification.setLink("/grade-review/" + gradeReview.getId() + "/edit");
                                notification.setIsRead(false);
                                notifications.add(notification);

                                receivers.add(u.getLogin());
                            });

                        notificationDTO.setReceivers(receivers);
                        notificationRepository.saveAll(notifications);
                    });
            });

        return notificationDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        NotificationDTO notificationDTO = new NotificationDTO();
        messagingTemplate.convertAndSend("/notification/public", notificationDTO);
    }
}