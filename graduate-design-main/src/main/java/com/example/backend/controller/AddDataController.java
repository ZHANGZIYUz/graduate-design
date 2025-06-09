package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.model.domain.*;
import com.example.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/addData")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class AddDataController {

    @Resource
    private EmploymentApprovalService employmentApprovalService;

    @Resource
    private ModuleService moduleService;

    @Resource
    private RoomService roomService;

    @Resource
    private TaService taService;

    @Resource
    private TeachingSessionService teachingSessionService;

    @Resource
    private TimetableEntryService timetableEntryService;

    @PostMapping("/addEmploymentApproval")
    public Result<String> addEmploymentApproval(EmploymentApproval employmentApproval) {
        boolean save = employmentApprovalService.save(employmentApproval);
        if (save == true) {
            return new Result<>(0, "success", "success add EmploymentApproval");
        } else {
            return new Result<>(1, "fail", "fail to add EmploymentApproval");
        }
    }

    @PostMapping("/addModule")
    public Result<String> addModule(Module module) {
        boolean save = moduleService.save(module);
        if (save == true) {
            return new Result<>(0, "success", "success add Module");
        } else {
            return new Result<>(1, "fail", "fail to add Module");
        }
    }

    @PostMapping("/addRoom")
    public Result<String> addRoom(Room room) {
        boolean save = roomService.save(room);
        if (save == true) {
            return new Result<>(0, "success", "success add Room");
        } else {
            return new Result<>(1, "fail", "fail to add Room");
        }
    }

    @PostMapping("/addTa")
    public Result<String> addTa(Ta ta) {
        boolean save = taService.save(ta);
        if (save == true) {
            return new Result<>(0, "success", "success add Ta");
        } else {
            return new Result<>(1, "fail", "fail to add Ta");
        }
    }

    @PostMapping("/addTeachingSeesion")
    public Result<String> addTeachingSession(TeachingSession teachingSession) {
        boolean save = teachingSessionService.save(teachingSession);
        if (save == true) {
            return new Result<>(0, "success", "success add TeachingSession");
        } else {
            return new Result<>(1, "fail", "fail to add TeachingSession");
        }
    }

    @PostMapping("/addTimetableEntry")
    public Result<String> addTimetableEntry(TimetableEntry timetableEntry) {
        boolean save = timetableEntryService.save(timetableEntry);
        if (save == true) {
            return new Result<>(0, "success", "success add TimetableEntry");
        } else {
            return new Result<>(1, "fail", "fail to add TimetableEntry");
        }
    }

}
