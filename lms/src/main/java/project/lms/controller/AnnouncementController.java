package project.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.lms.model.Announcement;
import project.lms.model.Member;
import project.lms.repository.MemberRepository;
import project.lms.service.AnnouncementService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class AnnouncementController {

    private final AnnouncementService announcementService;
    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/announcement")
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/announcement/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> saveAnnouncement(@RequestBody Announcement announcement) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByLoginId(username); // findByUsername에서 findByLoginId로 변경
        Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement, member);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PutMapping("/announcement/update/{announcementId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable Long announcementId, @RequestBody Announcement announcement) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByLoginId(username); // findByUsername에서 findByLoginId로 변경
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(announcement, member);
        return new ResponseEntity<>(updatedAnnouncement, HttpStatus.OK); // 테스트할 때 일단 updatedAnnouncement 부분을 null로 바꾸고 하기
    }

    @DeleteMapping("/announcement/delete/{announcementId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.deleteAnnouncement(announcementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
