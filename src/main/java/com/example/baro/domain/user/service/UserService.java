package com.example.baro.domain.user.service;

import com.example.baro.domain.user.UserRepository;
import com.example.baro.domain.user.dto.response.HomeResponseDto;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PromisePersonalRepository promisePersonalRepository;
	private final PromiseRepository promiseRepository;

	public HomeResponseDto getHomePageInfo() {
		// 임시 사용자 정보
		String username = "김철수";

		// 다가오는 일정 조회
//		List<HomeResponseDto.UpcomingScheduleDto> upcomingSchedules = scheduleRepository.findUpcomingSchedules();

		// 다가오는 약속 조회
//		PromiseSuggestResponseDto upcomingDday = promiseRepository.findUpcomingDday();

		// 참여자 정보
//		List<String> participants = userRepository.findParticipantsForUser(1L);

//		return MainPageResponseDto.builder()
//				.name(username)
//				.upcomingDday(upcomingDday)
//				.upcomingSchedules(upcomingSchedules)
//				.participants(participants)
//				.build();
		return null;
	}
}
