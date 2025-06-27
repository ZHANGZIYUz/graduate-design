package com.example.backend.controller;

import com.example.backend.common.*;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.*;
import com.example.backend.model.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/datasetGenerator")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class DataSetGenerator {

    @Resource
    private TaMapper taMapper;

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private TeachingSessionMapper teachingSessionMapper;

    @Resource
    private TimetableEntryMapper timetableEntryMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private EmploymentApprovalMapper employmentApprovalMapper;

    @Resource
    private SessionOccurrenceMapper sessionOccurrenceMapper;

    @Resource
    private TaAllocationMapper taAllocationMapper;

    @PostMapping("/generate")
    public Result<List> generateData(@RequestParam int TaNumber, @RequestParam int ModuleNumber,
                                     @RequestParam int RoomNumber) {
        List<Ta> taList = new ArrayList<>();
        List<Module> moduleList = new ArrayList<>();
        List<TeachingSession> teachingSessionList = new ArrayList<>();
        List<Room> roomList = new ArrayList<>();
        List<TimetableEntry> timetableEntryList = new ArrayList<>();
        List<EmploymentApproval> employmentApprovalList = new ArrayList<>();

        for (int i = 0; i < TaNumber; i++) {
            Ta ta = new Ta("ta" + i, ThreadLocalRandom.current().nextInt(8, 12),
                    ThreadLocalRandom.current().nextInt(240, 360));
            taList.add(ta);
            taMapper.insert(ta);
        }

        for (int i = 0; i < ModuleNumber; i++) {
            Module module = new Module("module" + i, "cs" + i);
            moduleList.add(module);
            moduleMapper.insert(module);
        }

        for (Module module : moduleList) {
            for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 3); i++) {
                TeachingSession teachingSession = new TeachingSession(module.getId(),
                        "TeachingSession" + module.getName() + i, ThreadLocalRandom.current().nextInt(1, 2),
                        ThreadLocalRandom.current().nextInt(1, 3), generateNumberString(ThreadLocalRandom.current().nextInt(1, 7))
                );
                teachingSessionList.add(teachingSession);
                teachingSessionMapper.insert(teachingSession);
            }
        }

        for (int i = 0; i < RoomNumber; i++) {
            Room room = new Room(getRandomCampus(), getRandomLocation(),
                    ThreadLocalRandom.current().nextInt(20, 50));
            roomList.add(room);
            roomMapper.insert(room);
        }

        for (TeachingSession teachingSession : teachingSessionList) {
            String time_table_weeks = teachingSession.getTime_table_weeks();
            int timetableEntryPerSession = this.getTotalWeek(time_table_weeks);
            for (int i = 0; i < timetableEntryPerSession; i++) {
                List<String> randomStartAndEndTime = getRandomStartAndEndTime(teachingSession.getId());
                TimetableEntry timetableEntry = new TimetableEntry(teachingSession.getId(),
                        (long) this.getRandomRoomId(), (i + 1) + "-" + getRandomDay(),
                        randomStartAndEndTime.get(0), randomStartAndEndTime.get(1)
                );
                timetableEntryList.add(timetableEntry);
                timetableEntryMapper.insert(timetableEntry);
            }
        }

        for (Ta ta : taList) {
            for (Module module : moduleList) {
                EmploymentApproval employmentApproval = new EmploymentApproval(ta.getId(), module.getId(), EmploymentRating.GREEN/*getRandomRating()*/);
                employmentApprovalList.add(employmentApproval);
                employmentApprovalMapper.insert(employmentApproval);
            }
        }

        return new Result<>(0, timetableEntryList, "insert data successfully");
    }

    @DeleteMapping("/deleteAll")
    public Result<String> deleteAllData() {
        taMapper.delete(null);
        moduleMapper.delete(null);
        teachingSessionMapper.delete(null);
        timetableEntryMapper.delete(null);
        roomMapper.delete(null);
        employmentApprovalMapper.delete(null);
        sessionOccurrenceMapper.delete(null);
        taAllocationMapper.delete(null);
        return new Result<>(0, "success", "successfully delete all Data");
    }

    public static String generateNumberString(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 1; i <= n; i++) {
            sb.append(i);
            if (i != n) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static int getTotalWeek(String timetableWeek) {
        String[] split = timetableWeek.split(",");
        return split.length;
    }

    public static String getRandomCampus() {
        Campus[] values = Campus.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index].toString();
    }

    public static String getRandomLocation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ThreadLocalRandom.current().nextInt(1, 5));
        stringBuilder.append(0);
        stringBuilder.append(ThreadLocalRandom.current().nextInt(1, 9));
        return stringBuilder.toString();
    }

    public static String getRandomDay() {
        Day[] values = Day.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index].toString();
    }

    public List<String> getRandomStartAndEndTime(Long teachingSessionId) {
        List<String> list = new ArrayList<>();
        TeachingSession teachingSession = teachingSessionMapper.selectById(teachingSessionId);
        StringBuilder startStringBuilder = new StringBuilder();
        StringBuilder endStringBuilder = new StringBuilder();
        int startTime = ThreadLocalRandom.current().nextInt(9, 17);
        int endTime = startTime + teachingSession.getHoursPaidPerOccurrence();
        startStringBuilder.append(startTime);
        startStringBuilder.append(":00");
        endStringBuilder.append(endTime);
        endStringBuilder.append(":00");
        list.add(startStringBuilder.toString());
        list.add(endStringBuilder.toString());
        return list;
    }

    public Long getRandomRoomId() {
        List<Room> roomList = roomMapper.selectList(null);
        if (roomList.isEmpty()) {
            throw new MyException(ErrorCode.PARAMS_ERROR, "roomList is empty");
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(roomList.size());
        return roomList.get(randomIndex).getId();
    }


    public String getRandomRating() {
        EmploymentRating[] values = EmploymentRating.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index].toString();
    }

}
