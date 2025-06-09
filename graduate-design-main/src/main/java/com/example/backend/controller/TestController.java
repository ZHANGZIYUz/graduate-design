package com.example.backend.controller;

import com.example.backend.mapper.*;
import com.example.backend.model.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class TestController {
    @Resource
    private TaMapper taMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private TeachingSessionMapper teachingSessionMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private TimetableEntryMapper timetableEntryMapper;
    @Resource
    private EmploymentApprovalMapper employmentApprovalMapper;

    @PostMapping("/insertExtendedTestData")
    public String insertExtendedTestData() {
        // Insert TAs
        Ta ta1 = new Ta();
        ta1.setId(1L);
        ta1.setName("Alice");
        ta1.setMaxHoursPerWeek(6);
        ta1.setMaxHoursPerYear(100);
        taMapper.insert(ta1);
        Ta ta2 = new Ta();
        ta2.setId(2L);
        ta2.setName("Bob");
        ta2.setMaxHoursPerWeek(6);
        ta2.setMaxHoursPerYear(100);
        taMapper.insert(ta2);
        Ta ta3 = new Ta();
        ta3.setId(3L);
        ta3.setName("Charlie");
        ta3.setMaxHoursPerWeek(6);
        ta3.setMaxHoursPerYear(100);
        taMapper.insert(ta3);
        Ta ta4 = new Ta();
        ta4.setId(4L);
        ta4.setName("Diana");
        ta4.setMaxHoursPerWeek(6);
        ta4.setMaxHoursPerYear(100);
        taMapper.insert(ta4);

        // Insert Modules
        Module m1 = new Module();
        m1.setId(1L);
        m1.setName("CS101 - Java");
        m1.setCode("CS101");
        moduleMapper.insert(m1);
        Module m2 = new Module();
        m2.setId(2L);
        m2.setName("CS102 - Python");
        m2.setCode("CS102");
        moduleMapper.insert(m2);

        // Insert Teaching Sessions
        TeachingSession s1 = new TeachingSession();
        s1.setId(1L);
        s1.setName("Java Lab A");
        s1.setModuleId(1L);
        s1.setTime_table_weeks("[1,2]");
        s1.setNumTAsPerSession(1);
        s1.setHoursPaidPerOccurrence(1);
        teachingSessionMapper.insert(s1);
        TeachingSession s2 = new TeachingSession();
        s2.setId(2L);
        s2.setName("Java Lab B");
        s2.setModuleId(1L);
        s2.setTime_table_weeks("[1,2]");
        s2.setNumTAsPerSession(1);
        s2.setHoursPaidPerOccurrence(1);
        teachingSessionMapper.insert(s2);
        TeachingSession s3 = new TeachingSession();
        s3.setId(3L);
        s3.setName("Python Lab A");
        s3.setModuleId(2L);
        s3.setTime_table_weeks("[1,2]");
        s3.setNumTAsPerSession(1);
        s3.setHoursPaidPerOccurrence(1);
        teachingSessionMapper.insert(s3);
        TeachingSession s4 = new TeachingSession();
        s4.setId(4L);
        s4.setName("Python Lab B");
        s4.setModuleId(2L);
        s4.setTime_table_weeks("[1,2]");
        s4.setNumTAsPerSession(1);
        s4.setHoursPaidPerOccurrence(1);
        teachingSessionMapper.insert(s4);

        // Insert Rooms
        Room r1 = new Room();
        r1.setId(1L);
        r1.setLocation("Room A");
        r1.setCampus("Main");
        roomMapper.insert(r1);
        Room r2 = new Room();
        r2.setId(2L);
        r2.setLocation("Room B");
        r2.setCampus("North");
        roomMapper.insert(r2);

        // Insert Timetable Entries
        TimetableEntry t1 = new TimetableEntry();
        t1.setId(1L);
        t1.setSessionId(1L);
        t1.setWeekDay("1-Monday");
        t1.setStartTime("09:00");
        t1.setEndTime("10:00");
        t1.setRoomId(1L);
        timetableEntryMapper.insert(t1);
        TimetableEntry t2 = new TimetableEntry();
        t2.setId(2L);
        t2.setSessionId(2L);
        t2.setWeekDay("1-Monday");
        t2.setStartTime("09:30");
        t2.setEndTime("10:30");
        t2.setRoomId(1L);
        timetableEntryMapper.insert(t2);
        TimetableEntry t3 = new TimetableEntry();
        t3.setId(3L);
        t3.setSessionId(3L);
        t3.setWeekDay("1-Tuesday");
        t3.setStartTime("10:00");
        t3.setEndTime("11:00");
        t3.setRoomId(1L);
        timetableEntryMapper.insert(t3);
        TimetableEntry t4 = new TimetableEntry();
        t4.setId(4L);
        t4.setSessionId(4L);
        t4.setWeekDay("1-Tuesday");
        t4.setStartTime("11:15");
        t4.setEndTime("12:15");
        t4.setRoomId(2L);
        timetableEntryMapper.insert(t4);
        TimetableEntry t5 = new TimetableEntry();
        t5.setId(5L);
        t5.setSessionId(1L);
        t5.setWeekDay("2-Monday");
        t5.setStartTime("09:00");
        t5.setEndTime("10:00");
        t5.setRoomId(1L);
        timetableEntryMapper.insert(t5);
        TimetableEntry t6 = new TimetableEntry();
        t6.setId(6L);
        t6.setSessionId(2L);
        t6.setWeekDay("2-Monday");
        t6.setStartTime("09:30");
        t6.setEndTime("10:30");
        t6.setRoomId(1L);
        timetableEntryMapper.insert(t6);
        TimetableEntry t7 = new TimetableEntry();
        t7.setId(7L);
        t7.setSessionId(3L);
        t7.setWeekDay("2-Tuesday");
        t7.setStartTime("10:00");
        t7.setEndTime("11:00");
        t7.setRoomId(1L);
        timetableEntryMapper.insert(t7);
        TimetableEntry t8 = new TimetableEntry();
        t8.setId(8L);
        t8.setSessionId(4L);
        t8.setWeekDay("2-Tuesday");
        t8.setStartTime("11:15");
        t8.setEndTime("12:15");
        t8.setRoomId(2L);
        timetableEntryMapper.insert(t8);

        // Insert Employment Approvals
        EmploymentApproval e1 = new EmploymentApproval();
        e1.setId(1L);
        e1.setTaId(1L);
        e1.setModuleId(1L);
        e1.setRating("GREEN");
        employmentApprovalMapper.insert(e1);
        EmploymentApproval e2 = new EmploymentApproval();
        e2.setId(2L);
        e2.setTaId(2L);
        e2.setModuleId(1L);
        e2.setRating("GREEN");
        employmentApprovalMapper.insert(e2);
        EmploymentApproval e3 = new EmploymentApproval();
        e3.setId(3L);
        e3.setTaId(2L);
        e3.setModuleId(2L);
        e3.setRating("GREEN");
        employmentApprovalMapper.insert(e3);
        EmploymentApproval e4 = new EmploymentApproval();
        e4.setId(4L);
        e4.setTaId(3L);
        e4.setModuleId(2L);
        e4.setRating("GREEN");
        employmentApprovalMapper.insert(e4);
        EmploymentApproval e5 = new EmploymentApproval();
        e5.setId(5L);
        e5.setTaId(4L);
        e5.setModuleId(2L);
        e5.setRating("RED");
        employmentApprovalMapper.insert(e5);

        return "Extended test data inserted successfully.";
    }
}
