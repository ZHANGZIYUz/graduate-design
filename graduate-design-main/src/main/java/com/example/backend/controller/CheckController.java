package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.common.ErrorCode;
import com.example.backend.common.Result;
import com.example.backend.exception.MyException;
import com.example.backend.model.domain.*;
import com.example.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/check")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class CheckController {
    @Resource
    private TaService taService;

    @Resource
    private EmploymentApprovalService employmentApprovalService;

    @Resource
    private TaAllocationService taAllocationService;

    @Resource
    private TeachingSessionService teachingSessionService;

    @Resource
    private TimetableEntryService timetableEntryService;

    @Resource
    private RoomService roomService;

    @PostMapping("/workingTime")
    public Result<String> checkMaxWorkingTime() {
        List<Ta> taList = taService.list();
        for (Ta ta : taList) {
            Integer maxHoursPerWeek = ta.getMaxHoursPerWeek();
            QueryWrapper<TaAllocation> queryWrapper = new QueryWrapper();
            queryWrapper.eq("taId", ta.getId());
            List<TaAllocation> taAllocationList = taAllocationService.list(queryWrapper).stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(TaAllocation::getSessionId, Function.identity(), (a, b) -> a),
                            map -> new ArrayList<>(map.values())
                    ));
            Integer totalWorkingTimePerWeek = 0;
            for (TaAllocation taAllocation : taAllocationList) {
                Long sessionId = taAllocation.getSessionId();
                QueryWrapper<TeachingSession> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", sessionId);
                TeachingSession teachingSession = teachingSessionService.getOne(queryWrapper1);
                totalWorkingTimePerWeek = totalWorkingTimePerWeek + teachingSession.getHoursPaidPerOccurrence();
                ;
            }
            if (totalWorkingTimePerWeek > maxHoursPerWeek) {
                throw new MyException(ErrorCode.ALLOCATE_WORKING_HOUR_ERROR, "TA's(taId = " + ta.getId() +
                        ") working hours exceed the allowed limit");
            }
        }
        return new Result<>(0, "Working hours are correct",
                "Working hours are correct");
    }

    @PostMapping("/employmentApproval")
    public Result<String> checkEmploymentApproval() {
        List<Ta> taList = taService.list();
        for (Ta ta : taList) {
            QueryWrapper<TaAllocation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taId", ta.getId());
            List<TaAllocation> taAllocationList = taAllocationService.list(queryWrapper);
            Map<Long, Long> employmentApproval = taAllocationList.stream().collect(Collectors.toMap(
                    TaAllocation::getTaId,
                    TaAllocation::getModuleId,
                    (existing, replacement) -> existing
            ));
            for (Map.Entry<Long, Long> entry : employmentApproval.entrySet()) {
                Long taId = entry.getKey();
                Long moduleId = entry.getValue();
                QueryWrapper<EmploymentApproval> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("taId", taId).eq("moduleId", moduleId);
                EmploymentApproval one = employmentApprovalService.getOne(queryWrapper1);
                if ("RED".equals(one.getRating().toString())) {
                    throw new MyException(ErrorCode.ALLOCATE_EMPLOYMENT_APPROVAL_ERROR, "This TA(taId = " + ta.getId() +
                            ") is not authorized to teach this module");
                }
            }
        }
        return new Result<>(0, "All TA permissions are verified successfully",
                "All TA permissions are verified successfully");
    }

    @PostMapping("/timeConflict")
    public Result<String> checkTimeConflict() {
        List<Ta> taList = taService.list();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        for (Ta ta : taList) {
            QueryWrapper<TaAllocation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taId", ta.getId());
            List<Long> timetableEntryIdList = taAllocationService.list(queryWrapper).stream()
                    .map(TaAllocation::getTimetableEntryId)
                    .collect(Collectors.toList());
            QueryWrapper<TimetableEntry> queryWrapper1 = new QueryWrapper<>();
            if (timetableEntryIdList.isEmpty()) {
                continue;
            }
            queryWrapper1.in("id", timetableEntryIdList);
            List<TimetableEntry> timetableEntryList = timetableEntryService.list(queryWrapper1);
            List<TimetableEntry> preTimeTableEntry = new ArrayList<>();
            for (TimetableEntry entry : timetableEntryList) {
                for (TimetableEntry previous : preTimeTableEntry) {
                    if (entry.getWeekDay().equals(previous.getWeekDay())) {
                        LocalTime start1 = LocalTime.parse(entry.getStartTime(), formatter);
                        LocalTime end1 = LocalTime.parse(entry.getEndTime(), formatter);
                        LocalTime start2 = LocalTime.parse(previous.getStartTime(), formatter);
                        LocalTime end2 = LocalTime.parse(previous.getEndTime(), formatter);

                        if (start1.isBefore(end2) && start2.isBefore(end1)) {
                            throw new MyException(
                                    ErrorCode.ALLOCATE_TIME_CONFLICT_ERROR,
                                    "Time conflict detected for TA ID " + ta.getId() +
                                            " on " + entry.getWeekDay() +
                                            " between " + entry.getStartTime() + "-" + entry.getEndTime() +
                                            " and " + previous.getStartTime() + "-" + previous.getEndTime()
                            );
                        }
                    }
                }
                preTimeTableEntry.add(entry);
            }
        }
        return new Result<>(0, "No time conflicts found for any TA",
                "No time conflicts found for any TA");
    }

    @PostMapping("/spaceConflict")
    public Result<String> checkSpaceConflict() {
        List<Ta> taList = taService.list();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        for (Ta ta : taList) {
            QueryWrapper<TaAllocation> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("taId", ta.getId());
            List<Long> timetableEntryIdList = taAllocationService.list(queryWrapper1).stream()
                    .map(TaAllocation::getTimetableEntryId)
                    .collect(Collectors.toList());
            QueryWrapper<TimetableEntry> queryWrapper2 = new QueryWrapper<>();
            if (timetableEntryIdList.isEmpty()) {
                continue;
            }
            queryWrapper2.in("id", timetableEntryIdList);
            List<TimetableEntry> timetableEntryList = timetableEntryService.list(queryWrapper2);
            List<TimetableEntry> preTimeTableEntry = new ArrayList<>();
            for (TimetableEntry entry : timetableEntryList) {
                for (TimetableEntry previous : preTimeTableEntry) {
                    if (entry.getWeekDay().equals(previous.getWeekDay()) && (!this.checkRoom(previous.getRoomId(), entry.getRoomId()))) {
                        LocalTime start1 = LocalTime.parse(entry.getStartTime(), formatter);
                        LocalTime end1 = LocalTime.parse(entry.getEndTime(), formatter);
                        LocalTime start2 = LocalTime.parse(previous.getStartTime(), formatter);
                        LocalTime end2 = LocalTime.parse(previous.getEndTime(), formatter);

                        long gap1 = Duration.between(end1, start2).toMinutes();
                        long gap2 = Duration.between(end2, start1).toMinutes();

                        if ((start1.isBefore(start2) && gap1 <= 60) ||
                                (start2.isBefore(start1) && gap2 <= 60)) {
                            QueryWrapper<Room> queryWrapper3 = new QueryWrapper<>();
                            queryWrapper3.eq("id", entry.getRoomId());
                            QueryWrapper<Room> queryWrapper4 = new QueryWrapper<>();
                            queryWrapper4.eq("id", previous.getRoomId());
                            Room entryRoom = roomService.getOne(queryWrapper3);
                            Room preRoom = roomService.getOne(queryWrapper4);
                            throw new MyException(
                                    ErrorCode.ALLOCATE_SPACE_CONFLICT_ERROR,
                                    "Space conflict detected for TA ID " + ta.getId() +
                                            " on " + entry.getWeekDay() +
                                            " between " + entry.getStartTime() + "-" + entry.getEndTime() +
                                            " at " + entryRoom.getCampus() +
                                            " and " + previous.getStartTime() + "-" + previous.getEndTime() +
                                            " at " + preRoom.getCampus()
                            );
                        }
                    }
                }
                preTimeTableEntry.add(entry);
            }
        }
        return new Result<>(0, "No space conflicts found for any TA",
                "No space conflicts found for any TA");
    }

    @PostMapping("/taNumberForEachSession")
    public Result<String> checkTaNumberForEachSession() {
        List<TeachingSession> teachingSessionList = teachingSessionService.list();
        for (TeachingSession teachingSession : teachingSessionList) {
            Integer numTAsPerSession = teachingSession.getNumTAsPerSession();
            QueryWrapper<TaAllocation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sessionId", teachingSession.getId());
            Set taId = taAllocationService.list(queryWrapper)
                    .stream().map(TaAllocation::getTaId)
                    .collect(Collectors.toSet());
            if (taId.size() != numTAsPerSession) {
                throw new MyException(ErrorCode.ALLOCATE_TA_NUMBER_EACH_SESSION_ERROR,
                        "Assigned TA count for this session " + teachingSession.getId() +
                                " is wrong " + taId.size() +
                                " , right data should be " + teachingSession.getNumTAsPerSession());
            }
        }
        return new Result<>(0, "The number of TA for each Session is correct",
                "The number of TA for each Session is correct");
    }

    public boolean checkRoom(Long room1, Long room2) {
        QueryWrapper<Room> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<Room> queryWrapper2 = new QueryWrapper<>();
        queryWrapper1.eq("id", room1);
        queryWrapper2.eq("id", room2);
        Room firstRoom = roomService.getOne(queryWrapper1);
        Room secondRoom = roomService.getOne(queryWrapper2);
        return firstRoom.getCampus().equals(secondRoom.getCampus());
    }

}
