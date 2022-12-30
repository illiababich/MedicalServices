package com.medicalservices.timeslot.controller;

import com.medicalservices.timeslot.model.Timeslot;
import com.medicalservices.timeslot.model.TimeslotDto;
import com.medicalservices.timeslot.model.TimeslotRequest;
import com.medicalservices.timeslot.service.TimeslotService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/timeslots", produces = "application/json")
public class TimeslotController {
  private final TimeslotService timeslotService;

  @GetMapping
  public Map<String, List<TimeslotDto>> getAllTimeslots() {
    return timeslotService.getAllTimeslots();
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN', 'SCOPE_PATIENT', 'SCOPE_SERVICE_PROVIDER')")
  @GetMapping(params = {"available=true", "groupedBy=start_time_date"})
  public Map<String, List<TimeslotDto>> getAvailableTimeslotsGroupedByStartTime() {
    return timeslotService.getGroupedAvailableTimeslots();
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN', 'SCOPE_SERVICE_PROVIDER')")
  @PostMapping(consumes = "application/json")
  public Collection<Timeslot> createTimeslots(@RequestBody List<TimeslotRequest> timeslots) {
    return timeslotService.createTimeslots(timeslots);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
  @GetMapping(path = "/{timeslotId}")
  public TimeslotDto getTimeslotById(@PathVariable Long timeslotId) {
    return timeslotService.getTimeslotById(timeslotId);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
  @DeleteMapping(path = "/{timeslotId}")
  public void deleteTimeslotById(@PathVariable Long timeslotId) {
    timeslotService.deleteTimeslotById(timeslotId);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER', 'SCOPE_ADMIN')")
  @PatchMapping(path = "/{timeslotId}", consumes = "application/json")
  public Timeslot patchTimeslotById(@PathVariable Long timeslotId, @RequestBody TimeslotRequest timeslotPatch) {
    return timeslotService.patchTimeslotById(timeslotId, timeslotPatch);
  }
}
