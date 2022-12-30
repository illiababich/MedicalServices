package com.medicalservices.timeslot.service;

import com.medicalservices.serviceprovider.model.ServiceProvider;
import com.medicalservices.timeslot.mapper.TimeslotMapper;
import com.medicalservices.serviceprovider.repository.ServiceProviderRepository;
import com.medicalservices.timeslot.model.Timeslot;
import com.medicalservices.timeslot.model.TimeslotDto;
import com.medicalservices.timeslot.model.TimeslotRequest;
import com.medicalservices.timeslot.repository.TimeslotRepository;
import com.medicalservices.timeslot.utils.SortByDateString;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeslotService {
  private final TimeslotRepository timeslotRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public Map<String, List<TimeslotDto>> getAllTimeslots() {
    return groupTimeslotsByStartTimeDate(getTimeslots());
  }

  public Map<String, List<TimeslotDto>> getGroupedAvailableTimeslots() {
    return groupTimeslotsByStartTimeDate(getAvailableTimeslots());
  }

  private List<TimeslotDto> getAvailableTimeslots() {
    return timeslotRepository.findAvailableTimeslots().stream()
      .map(TimeslotMapper::toDto).collect(Collectors.toList());
  }

  private List<TimeslotDto> getTimeslots() {
    return timeslotRepository.findAll().stream().map(TimeslotMapper::toDto)
      .collect(Collectors.toList());
  }

  private Map<String, List<TimeslotDto>> groupTimeslotsByStartTimeDate(List<TimeslotDto> timeslotDtos) {
    Map<String, List<TimeslotDto>> groupedTimeslots = timeslotDtos.stream()
      .collect(Collectors.groupingBy(TimeslotDto::getStartTimeDate));

    Map<String, List<TimeslotDto>> sortedGroupedTimeslots =
      new TreeMap<>(new SortByDateString());
    sortedGroupedTimeslots.putAll(groupedTimeslots);

    return sortedGroupedTimeslots;
  }

  public List<Timeslot> createTimeslots(List<TimeslotRequest> timeslotRequest) {
    ServiceProvider serviceProvider = serviceProviderRepository.findById(timeslotRequest.get(0).getServiceProviderId())
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));

    List<Timeslot> timeslots =  timeslotRequest.stream().map(timeslot -> Timeslot.builder()
      .startTime(timeslot.getStartTime())
      .endTime(timeslot.getEndTime())
      .serviceProvider(serviceProvider)
      .build()).collect(
      Collectors.toList());
    return timeslotRepository.saveAll(timeslots);
  }

  public TimeslotDto getTimeslotById(Long timeslotId) {
    return timeslotRepository.findById(timeslotId).map(TimeslotMapper::toDto)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND));
  }

  public void deleteTimeslotById(Long timeslotId) {
    timeslotRepository.delete(timeslotRepository.findById(timeslotId)
      .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND)));
  }

  public Timeslot patchTimeslotById(Long timeslotId, TimeslotRequest patch) {
    Timeslot updateTimeslot = timeslotRepository.findById(timeslotId)
      .map((timeslot) -> timeslot.toBuilder()
        .serviceProvider((null == patch.getServiceProviderId())
          ? timeslot.getServiceProvider()
          : Optional.of(serviceProviderRepository.findById(patch.getServiceProviderId()).get())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND)))
        .endTime(Optional.ofNullable(patch.getEndTime()).orElse(timeslot.getEndTime()))
        .startTime(Optional.ofNullable(patch.getStartTime()).orElse(timeslot.getStartTime()))
        .build()
      ).orElseThrow(
        () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "The Timeslot with provided ID not found.")
      );

    timeslotRepository.save(updateTimeslot);
    return updateTimeslot;
  }
}
