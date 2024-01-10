package com.ptudw.web.web.websocket;

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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class PushNotificationService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final AssignmentService assignmentService;

    private final UserRepository userRepository;

    private final GradeReviewService gradeReviewService;

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
        this.userRepository = userRepository;
        this.gradeReviewService = gradeReviewService;
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

        String courseId = StringUtils.isNotBlank(notificationDTO.getCourseId())
            ? notificationDTO.getCourseId()
            : gradeReviewService
                .findOne(Long.valueOf(notificationDTO.getGradeReviewId()))
                .map(GradeReview::getCourseId)
                .map(id -> Long.toString(id))
                .orElse(null);

        userRepository
            .findOneByStudentId(notificationDTO.getStudentId())
            .ifPresent(u -> {
                Notification notification = new Notification();
                notification.setMessage(notificationDTO.getMessage());
                notification.setTopic(notificationDTO.getTopic());
                notification.setReceivers(u.getLogin());
                notification.setSender(principal.getName());
                notification.setLink("/course/" + courseId + "/detail/grade-review/" + notificationDTO.getGradeReviewId() + "/edit");
                notification.setIsRead(false);
                notificationRepository.save(notification);
            });

        return notificationDTO;
    }

    @MessageMapping("/notification-finalize-grade-composition")
    public NotificationDTO sendNotificationFinalizeGradeComposition(
        @Payload NotificationDTO notificationDTO,
        StompHeaderAccessor stompHeaderAccessor,
        Principal principal
    ) {
        log.debug("Sending notification finalize grade composition to student {}", notificationDTO);

        List<Long> ids = userCourseRepository
            .findAllByCourseId(Long.valueOf(notificationDTO.getCourseId()))
            .stream()
            .filter(Objects::nonNull)
            .map(userCourse -> userCourse.getUserId())
            .collect(Collectors.toList());

        notificationRepository.saveAll(
            userRepository
                .findAllWithAuthoritiesByIdIn(ids)
                .stream()
                .filter(Objects::nonNull)
                .filter(user ->
                    user
                        .getAuthorities()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Authority::getName)
                        .collect(Collectors.toList())
                        .contains(AuthoritiesConstants.STUDENT)
                )
                .map(user -> {
                    Notification notification = new Notification();
                    notification.setMessage(notificationDTO.getMessage());
                    notification.setTopic(notificationDTO.getTopic());
                    notification.setReceivers(user.getLogin());
                    notification.setSender(principal.getName());
                    notification.setLink("/course/" + notificationDTO.getCourseId() + "/detail/grade-structure");
                    notification.setIsRead(false);

                    return notification;
                })
                .collect(Collectors.toList())
        );

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
                                notification.setLink(
                                    "/course/" + gradeReview.getCourseId() + "/detail/grade-review/" + gradeReview.getId() + "/edit"
                                );
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
