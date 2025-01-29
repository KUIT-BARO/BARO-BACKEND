package com.example.baro.domain.user.service;

import com.example.baro.common.entity.Promise;
import com.example.baro.common.entity.PromisePersonal;
import com.example.baro.common.entity.User;
import com.example.baro.common.entity.UserPromise;
import com.example.baro.domain.user.repository.UserRepository;
import com.example.baro.domain.user.dto.response.HomeResponseDto;
import com.example.baro.domain.user.dto.response.UserPromiseResponseDto;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.UserPromiseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserPromiseRepository userPromiseRepository;
	private final PromisePersonalRepository promisePersonalRepository;

	@Transactional
	public HomeResponseDto getHomePageInfo(User user) {

		List<UserPromise> userPromises = userPromiseRepository.findAllByUserAndDisplayTrue(user);

		userPromises.sort(Comparator.comparing(up -> up.getPromise().getDate()));
		List<Promise> promises = userPromises.stream()
				.map(UserPromise::getPromise)
				.sorted(Comparator.comparing(Promise::getDate)).toList();

		if (promises.isEmpty()) {
			return HomeResponseDto.builder()
					.name(user.getNickname())
					.upcomingDday(null)
					.upcomingPromises(List.of()).build();
		}

		Promise upCommingPromise = promises.get(0);
		HomeResponseDto.UpcomingDdayDto upComingDdayDto = HomeResponseDto.UpcomingDdayDto.builder().promiseId(upCommingPromise.getId())
				.name(upCommingPromise.getName())
				.date(upCommingPromise.getDate())
				.timeStart(upCommingPromise.getTimeStart())
				.timeEnd(upCommingPromise.getTimeEnd())
				.place(upCommingPromise.getRegion().getName()).build();

		List<HomeResponseDto.UpcomingPromiseDto> upcomingPromiseDtos = new ArrayList<>();
		for (Promise promise : promises) {
			HomeResponseDto.UpcomingPromiseDto upcomingPromiseDto = HomeResponseDto.UpcomingPromiseDto.builder().promiseId(upCommingPromise.getId())
					.name(promise.getName())
					.date(promise.getDate())
					.timeStart(promise.getTimeStart())
					.timeEnd(promise.getTimeEnd())
					.place(promise.getRegion().getName())
					.peopleNumber(promise.getPeopleNumber()).build();
		}

		HomeResponseDto homeResponseDto = HomeResponseDto.builder()
				.name(user.getNickname())
				.upcomingDday(upComingDdayDto)
				.upcomingPromises(upcomingPromiseDtos).build();

		return homeResponseDto;
	}

	public UserPromiseResponseDto getPromisePageInfo(User user) {
		List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByUser(user);

		return null;
	}
}
