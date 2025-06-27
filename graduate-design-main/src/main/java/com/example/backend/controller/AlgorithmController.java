package com.example.backend.controller;

import com.example.backend.common.ErrorCode;
import com.example.backend.common.Result;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.*;
import com.example.backend.model.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/algorithm")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class AlgorithmController {

    @Resource
    private TaMapper taMapper;

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private TeachingSessionMapper teachingSessionMapper;

    @Resource
    private TimetableEntryMapper timetableEntryMapper;

    @Resource
    private EmploymentApprovalMapper employmentApprovalMapper;

    @Resource
    private TaAllocationMapper taAllocationMapper;

    @Resource
    private SessionOccurrenceMapper sessionOccurrenceMapper;

    @Resource
    private RoomMapper roomMapper;

    @PostMapping("/allocate")
    public Result<List<TaAllocation>> assignGreedy() {
        // flag represent the rollback times
        List<Ta> tas = taMapper.selectList(null);
        List<Module> modules = moduleMapper.selectList(null);
        List<EmploymentApproval> approvals = employmentApprovalMapper.selectList(null);
        List<TeachingSession> sessions = teachingSessionMapper.selectList(null);
        //sort TeachingSession by the number of TAs, the session which needs more TAs has high priority
        List<TeachingSession> sortedSessions = sessions.stream()
                .sorted(Comparator.comparing(TeachingSession::getNumTAsPerSession).reversed())
                .collect(Collectors.toList());
        List<TimetableEntry> allEntries = timetableEntryMapper.selectList(null);
        List<Room> rooms = roomMapper.selectList(null);

        if (tas.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "ta list is empty");
        }

        if (modules.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "module list is empty");
        }

        if (approvals.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "approval list is empty");
        }

        if (sessions.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "session list is empty");
        }

        if (allEntries.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "timetable entry list is empty");
        }

        if (rooms.isEmpty()) {
            throw new MyException(ErrorCode.NULL_ERROR, "room list is empty");
        }
        // return list
        List<TaAllocation> results = new ArrayList<>();
        // allocated Entry
        Set<Long> allocatedEntryIds = new HashSet<>();
        // taId -> allocated hours
        Map<Long, Integer> taHourMap = new HashMap<>();
        // taId -> allocated TimetableEntry List
        Map<Long, List<TimetableEntry>> taAllocatedEntries = new HashMap<>();
        // roomId -> Room
        Map<Long, Room> roomMap = rooms.stream()
                .collect(Collectors.toMap(Room::getId, r -> r));

        //If the allocation fails, try using four different sorting strategies
        for (int i = 0; i < 4; i++) {
            for (Module module : modules) {
                List<TeachingSession> moduleSessions = sortedSessions.stream()
                        .filter(s -> s.getModuleId().equals(module.getId()))
                        .collect(Collectors.toList());

                for (TeachingSession session : moduleSessions) {
                    List<TimetableEntry> sessionEntries = allEntries.stream()
                            .filter(e -> e.getSessionId().equals(session.getId()))
                            .collect(Collectors.toList());

                    List<Ta> eligibleTas = approvals.stream()
                            .filter(a -> a.getModuleId().equals(module.getId()) && !"RED".equalsIgnoreCase(String.valueOf(a.getRating())))
                            .map(a -> tas.stream().filter(ta -> ta.getId().equals(a.getTaId())).findFirst().orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    // use different sort strategy
                    List<Ta> sortedCandidates = sortTAs(eligibleTas, taHourMap, i % 4);
                    int needed = session.getNumTAsPerSession();
                    int assigned = 0;

                    while (assigned < needed) {
                        boolean allocatedThisRound = false;

                        for (Ta ta : sortedCandidates) {
                            int currentHours = taHourMap.getOrDefault(ta.getId(), 0);
                            if (currentHours + session.getHoursPaidPerOccurrence() > ta.getMaxHoursPerWeek()) {
                                continue;
                            }

                            // check conflict
                            boolean hasConflict = false;
                            List<TimetableEntry> prevEntries = taAllocatedEntries.getOrDefault(ta.getId(), new ArrayList<>());
                            for (TimetableEntry newEntry : sessionEntries) {
                                Room newRoom = roomMap.get(newEntry.getRoomId());
                                if (newRoom == null) continue;

                                for (TimetableEntry prevEntry : prevEntries) {
                                    Room prevRoom = roomMap.get(prevEntry.getRoomId());
                                    if (prevRoom == null) continue;

                                    String[] newEntryStartTime = newEntry.getStartTime().split(":");
                                    String[] newEntryEndTime = newEntry.getEndTime().split(":");
                                    String[] prevEntryStartTime = prevEntry.getStartTime().split(":");
                                    String[] prevEntryEndTime = prevEntry.getEndTime().split(":");

                                    if (prevEntry.getWeekDay().equals(newEntry.getWeekDay())) {
                                        boolean timeOverlap = false;
                                        if (Integer.valueOf(newEntryStartTime[0]) <= Integer.valueOf(prevEntryStartTime[0])
                                                && Integer.valueOf(newEntryEndTime[0]) >= Integer.valueOf(prevEntryStartTime[0])) {
                                            timeOverlap = true;
                                        }
                                        if (Integer.valueOf(prevEntryStartTime[0]) <= Integer.valueOf(newEntryStartTime[0])
                                                && Integer.valueOf(prevEntryEndTime[0]) >= Integer.valueOf(newEntryStartTime[0])) {
                                            timeOverlap = true;
                                        }
                                        if (Integer.parseInt(newEntryStartTime[0]) <= Integer.valueOf(prevEntryStartTime[0])
                                                && Integer.valueOf(newEntryEndTime[0]) >= Integer.valueOf(prevEntryEndTime[0])) {
                                            timeOverlap = true;
                                        }
                                        if (Integer.valueOf(prevEntryStartTime[0]) <= Integer.valueOf(newEntryStartTime[0])
                                                && Integer.valueOf(prevEntryEndTime[0]) >= Integer.valueOf(newEntryEndTime[0])) {
                                            timeOverlap = true;
                                        }
                                        if (timeOverlap) {
                                            hasConflict = true;
                                            break;
                                        }
                                    }

                                    if (!prevRoom.getCampus().equals(newRoom.getCampus()) &&
                                            prevEntry.getWeekDay().equals(newEntry.getWeekDay())) {

                                        if (Integer.parseInt(newEntryStartTime[0]) > Integer.valueOf(prevEntryEndTime[0])) {
                                            long diffMinutes = (Integer.valueOf(newEntryStartTime[0]) - Integer.valueOf(prevEntryEndTime[0])) * 60
                                                    + Integer.valueOf(newEntryStartTime[1]) - Integer.valueOf(prevEntryEndTime[1]);
                                            if (diffMinutes <= 60) {
                                                hasConflict = true;
                                                break;
                                            }
                                        }

                                        if (Integer.valueOf(prevEntryStartTime[0]) > Integer.valueOf(newEntryEndTime[0])) {
                                            long diffMinutes = (Integer.valueOf(prevEntryStartTime[0]) - Integer.valueOf(newEntryEndTime[0])) * 60
                                                    + Integer.valueOf(prevEntryStartTime[1]) - Integer.valueOf(newEntryEndTime[1]);
                                            if (diffMinutes <= 60) {
                                                hasConflict = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (hasConflict) break;

                                //allocate TimeTableEntry
                                newEntry.setAllocated(1);
                                newEntry.setId(newEntry.getId());
                                timetableEntryMapper.updateById(newEntry);
                            }

                            if (hasConflict) continue;

                            List<Integer> weekList = parseWeeks(session.getTime_table_weeks());
                            for (Integer week : weekList) {
                                SessionOccurrence occurrence = new SessionOccurrence();
                                occurrence.setSessionId(session.getId());
                                occurrence.setTaId(ta.getId());
                                occurrence.setTimeTableWeek(week);
                                int insert = sessionOccurrenceMapper.insert(occurrence);
                                if (insert == 0) {
                                    throw new MyException(ErrorCode.INSERT_ERROR, "Insert into table SessionOccurrence failed");
                                }
                            }

                            for (TimetableEntry entry : sessionEntries) {
                                allocatedEntryIds.add(entry.getId());
                                TaAllocation allocation = new TaAllocation();
                                allocation.setTaId(ta.getId());
                                allocation.setModuleId(module.getId());
                                allocation.setSessionId(session.getId());
                                allocation.setTimetableEntryId(entry.getId());
                                int insert = taAllocationMapper.insert(allocation);
                                if (insert == 0) {
                                    throw new MyException(ErrorCode.INSERT_ERROR, "Insert into table TaAllocation failed");
                                }
                                results.add(allocation);
                                taAllocatedEntries.computeIfAbsent(ta.getId(), k -> new ArrayList<>()).add(entry);
                            }

                            taHourMap.put(ta.getId(), currentHours + session.getHoursPaidPerOccurrence());
                            assigned++;
                            allocatedThisRound = true;
                            break;
                        }

                        if (!allocatedThisRound) {
                            break;
                        }
                    }
                }
            }

            List<TimetableEntry> timetableEntries = timetableEntryMapper.selectList(null);
            for (TimetableEntry timetableEntry : timetableEntries) {
                if (timetableEntry.getAllocated() == 1) {
                    continue;
                } else {
                    throw new MyException(ErrorCode.ALLOCATE_ERROR, "not all TimeTableEntries have been allocated");
                }
            }

            if (results == null) {
                throw new MyException(ErrorCode.NULL_ERROR, "allocation result is null");
            } else {
                return new Result<>(0, results, "allocate successfully");
            }
        }

        throw new MyException(ErrorCode.NULL_ERROR, "allocation result is null");
    }

    // ：change "[1,2,3]" into List<Integer>
    private List<Integer> parseWeeks(String weeksJson) {
        if (weeksJson == null || weeksJson.length() < 2) return Collections.emptyList();
        String weeks = weeksJson.substring(1, weeksJson.length() - 1).trim();
        if (weeks.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(weeks.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    // different sorting strategy
    public List<Ta> sortTAs(List<Ta> taList, Map<Long, Integer> taHourMap, int strategyType) {
        switch (strategyType) {
            case 1:
                return taList.stream()
                        .sorted((a, b) -> Integer.compare(b.getMaxHoursPerWeek(), a.getMaxHoursPerWeek()))
                        .collect(Collectors.toList());
            case 2:
                return taList.stream()
                        .sorted(Comparator.comparing(Ta::getName))
                        .collect(Collectors.toList());
            case 3:
                List<Ta> shuffled = new ArrayList<>(taList);
                Collections.shuffle(shuffled);
                return shuffled;
            default:
                return taList.stream()
                        .sorted((ta1, ta2) -> {
                            int h1 = taHourMap.getOrDefault(ta1.getId(), 0);
                            int h2 = taHourMap.getOrDefault(ta2.getId(), 0);
                            return Integer.compare(h1, h2);
                        })
                        .collect(Collectors.toList());
        }
    }
}
