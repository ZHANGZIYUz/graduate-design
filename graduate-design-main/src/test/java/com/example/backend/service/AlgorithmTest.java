package com.example.backend.service;

import com.example.backend.common.EmploymentRating;
import com.example.backend.common.Result;
import com.example.backend.controller.AlgorithmController;
import com.example.backend.model.domain.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class AlgorithmTest {

    @Resource
    private TaService taService;

    @Resource
    private ModuleService moduleService;

    @Resource
    private TeachingSessionService teachingSessionService;

    @Resource
    private TimetableEntryService timetableEntryService;

    @Resource
    private RoomService roomService;

    @Resource
    private EmploymentApprovalService employmentApprovalService;

    @Resource
    private AlgorithmController algorithmController;

    @Test
    public void insertTaData() {
        List<Ta> taList = new ArrayList<>();

        Ta Alice = new Ta();
        Alice.setId(1L);
        Alice.setName("Alice");
        Alice.setMaxHoursPerWeek(6);
        Alice.setMaxHoursPerYear(100);
        taList.add(Alice);

        Ta Bob = new Ta();
        Bob.setId(2L);
        Bob.setName("Bob");
        Bob.setMaxHoursPerWeek(8);
        Bob.setMaxHoursPerYear(100);
        taList.add(Bob);

        Ta Charlie = new Ta();
        Charlie.setId(3L);
        Charlie.setName("Charlie");
        Charlie.setMaxHoursPerWeek(4);
        Charlie.setMaxHoursPerYear(50);
        taList.add(Charlie);

        taService.saveBatch(taList);
    }

    @Test
    public void insertModule() {
        List<Module> modules = new ArrayList<>();

        Module bigData = new Module();
        bigData.setId(101L);
        bigData.setName("Big Data");
        bigData.setCode("CS101");
        modules.add(bigData);

        Module modelDrivenEngineering = new Module();
        modelDrivenEngineering.setId(102L);
        modelDrivenEngineering.setName("Model Driven Engineering");
        modelDrivenEngineering.setCode("CS102");
        modules.add(modelDrivenEngineering);

        moduleService.saveBatch(modules);
    }

    @Test
    public void insertTeachingSession() {
        List<TeachingSession> sessions = new ArrayList<>();

        TeachingSession teachingSession1 = new TeachingSession();
        teachingSession1.setId(201L);
        teachingSession1.setName("CS101 Tutorial");
        teachingSession1.setModuleId(101L);
        teachingSession1.setNumTAsPerSession(1);
        teachingSession1.setHoursPaidPerOccurrence(2);
        teachingSession1.setTime_table_weeks("[1,2]");
        sessions.add(teachingSession1);

        TeachingSession teachingSession2 = new TeachingSession();
        teachingSession2.setId(202L);
        teachingSession2.setName("CS102 Lab");
        teachingSession2.setModuleId(102L);
        teachingSession2.setNumTAsPerSession(1);
        teachingSession2.setHoursPaidPerOccurrence(2);
        teachingSession2.setTime_table_weeks("[1,2]");
        sessions.add(teachingSession2);

        teachingSessionService.saveBatch(sessions);
    }

    @Test
    public void insertTimetableEntry() {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        TimetableEntry timetableEntry1 = new TimetableEntry();
        timetableEntry1.setId(301L);
        timetableEntry1.setSessionId(201L);
        timetableEntry1.setRoomId(401L);
        timetableEntry1.setWeekDay("1-monday");
        timetableEntries.add(timetableEntry1);

        TimetableEntry timetableEntry2 = new TimetableEntry();
        timetableEntry2.setId(302L);
        timetableEntry2.setSessionId(201L);
        timetableEntry2.setRoomId(401L);
        timetableEntry2.setWeekDay("2-monday");
        timetableEntries.add(timetableEntry2);

        TimetableEntry timetableEntry3 = new TimetableEntry();
        timetableEntry1.setId(303L);
        timetableEntry1.setSessionId(202L);
        timetableEntry1.setRoomId(402L);
        timetableEntry1.setWeekDay("1-tuesday");
        timetableEntries.add(timetableEntry3);

        TimetableEntry timetableEntry4 = new TimetableEntry();
        timetableEntry1.setId(304L);
        timetableEntry1.setSessionId(202L);
        timetableEntry1.setRoomId(402L);
        timetableEntry1.setWeekDay("2-tuesday");
        timetableEntries.add(timetableEntry4);

        timetableEntryService.saveBatch(timetableEntries);
    }

    @Test
    public void insertRoom() {
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room();
        room1.setId(401L);
        room1.setCampus("waterloo");
        room1.setLocation("404");
        room1.setCapacity(40);
        rooms.add(room1);

        Room room2 = new Room();
        room1.setId(402L);
        room1.setCampus("waterloo");
        room1.setLocation("405");
        room1.setCapacity(80);
        rooms.add(room2);

        roomService.saveBatch(rooms);
    }

    @Test
    public void insertEmploymentApproval() {
        List<EmploymentApproval> employmentApprovals = new ArrayList<>();
        EmploymentApproval employmentApproval1 = new EmploymentApproval();
        employmentApproval1.setTaId(1L);
        employmentApproval1.setModuleId(101L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval1);

        EmploymentApproval employmentApproval2 = new EmploymentApproval();
        employmentApproval1.setTaId(1L);
        employmentApproval1.setModuleId(102L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval2);

        EmploymentApproval employmentApproval3 = new EmploymentApproval();
        employmentApproval1.setTaId(2L);
        employmentApproval1.setModuleId(101L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval3);

        EmploymentApproval employmentApproval4 = new EmploymentApproval();
        employmentApproval1.setTaId(2L);
        employmentApproval1.setModuleId(102L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval4);

        EmploymentApproval employmentApproval5 = new EmploymentApproval();
        employmentApproval1.setTaId(3L);
        employmentApproval1.setModuleId(101L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval5);

        EmploymentApproval employmentApproval6 = new EmploymentApproval();
        employmentApproval1.setTaId(3L);
        employmentApproval1.setModuleId(102L);
        employmentApproval1.setRating(EmploymentRating.GREEN);
        employmentApprovals.add(employmentApproval6);

        employmentApprovalService.saveBatch(employmentApprovals);
    }

    @Test
    public void testAlogrithm() {
        Result<List<TaAllocation>> listResult = algorithmController.assignGreedy();
        List<TaAllocation> list = listResult.getData();
        for (TaAllocation taAllocation : list) {
            System.out.println(taAllocation.getId() + " " + taAllocation.getTaId() + " "
                    + taAllocation.getModuleId() + " " + taAllocation.getTimetableEntryId());
        }
    }

}
