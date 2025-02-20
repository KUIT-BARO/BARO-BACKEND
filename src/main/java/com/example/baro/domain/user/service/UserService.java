package com.example.baro.domain.user.service;

import com.example.baro.common.Enum.userProfile.UserProfile;
import com.example.baro.common.entity.*;
import com.example.baro.domain.place.repository.SearchKeywordRepository;
import com.example.baro.domain.place.repository.SearchRepository;
import com.example.baro.domain.user.dto.request.ProfileImageChangeRequestDto;
import com.example.baro.domain.user.dto.response.MyPageResponseDto;
import com.example.baro.domain.user.repository.*;
import com.example.baro.domain.user.dto.response.HomeResponseDto;

import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.entity.Promise;
import com.example.baro.common.entity.PromisePersonal;
import com.example.baro.common.entity.User;
import com.example.baro.common.entity.UserPromise;
import com.example.baro.domain.user.dto.response.FindUserListResponseDto;
import com.example.baro.domain.user.dto.response.UserPromiseListResponseDto;
import com.example.baro.domain.user.repository.UserRepository;
import com.example.baro.domain.user.repository.PromisePersonalRepository;
import com.example.baro.domain.user.repository.UserPromiseRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserPromiseRepository userPromiseRepository;
	private final PromisePersonalRepository promisePersonalRepository;
	private final ScheduleRepository scheduleRepository;
	private final SearchKeywordRepository searchKeywordRepository;
	private final SearchRepository searchRepository;

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
				.purpose(upCommingPromise.getPurpose())
				.date(upCommingPromise.getDate())
				.timeStart(upCommingPromise.getTimeStart())
				.timeEnd(upCommingPromise.getTimeEnd())
				.place(upCommingPromise.getPlace().getName()).build();

		List<HomeResponseDto.UpcomingPromiseDto> upcomingPromiseDtos = new ArrayList<>();
		for (Promise promise : promises) {
			HomeResponseDto.UpcomingPromiseDto upcomingPromiseDto = HomeResponseDto.UpcomingPromiseDto.builder().promiseId(upCommingPromise.getId())
					.name(promise.getName())
					.purpose(promise.getPurpose())
					.date(promise.getDate())
					.timeStart(promise.getTimeStart())
					.timeEnd(promise.getTimeEnd())
					.place(promise.getPlace().getName())
					.peopleNumber(promise.getPeopleNumber()).build();
			upcomingPromiseDtos.add(upcomingPromiseDto);
		}

		return HomeResponseDto.builder()
				.name(user.getNickname())
				.upcomingDday(upComingDdayDto)
				.upcomingPromises(upcomingPromiseDtos).build();
	}

	public UserPromiseListResponseDto getPromisePageInfo(User user) {
		List<UserPromise> userPromises = userPromiseRepository.findAllByUserAndDisplayTrue(user);
		List<PromisePersonal> promisePersonals = promisePersonalRepository.findAllByUserAndStatus(user, Status.INACTIVE);

		promisePersonals.sort(Comparator.comparing(up -> up.getPromise().getDate()));
		List<Promise> pendingPromises = promisePersonals.stream()
				.map(PromisePersonal::getPromise)
				.sorted(Comparator.comparing(Promise::getDate)).toList();

		userPromises.sort(Comparator.comparing(up -> up.getPromise().getDate()));
		List<Promise> promises = userPromises.stream()
				.map(UserPromise::getPromise)
				.sorted(Comparator.comparing(Promise::getDate)).toList();


		List<UserPromiseListResponseDto.PendingPromisesDto> pendingPromiseDtos = new ArrayList<>();
		if (!pendingPromises.isEmpty()) {
			for (Promise promise : pendingPromises) {
				UserPromiseListResponseDto.PendingPromisesDto pendingPromisesDto = UserPromiseListResponseDto.PendingPromisesDto.builder()
						.promiseId(promise.getId())
						.name(promise.getName())
						.purpose(promise.getPurpose())
						.dateStart(promise.getDateStart())
						.dateEnd((promise.getDateEnd()))
						.place(promise.getPlace().getName())
						.peopleNumber(promise.getPeopleNumber()).build();
				pendingPromiseDtos.add(pendingPromisesDto);
			}
		}

		List<UserPromiseListResponseDto.UpcomingPromiseDto> upcomingPromiseDtos = new ArrayList<>();
		if (!promises.isEmpty()) {
			for (Promise promise : promises) {
				UserPromiseListResponseDto.UpcomingPromiseDto promiseDto = UserPromiseListResponseDto.UpcomingPromiseDto.builder()
						.promiseId(promise.getId())
						.name(promise.getName())
						.purpose(promise.getPurpose())
						.date(promise.getDate())
						.timeStart(promise.getTimeStart())
						.timeEnd(promise.getTimeEnd())
						.place(promise.getPlace().getName())
						.peopleNumber(promise.getPeopleNumber()).build();
				upcomingPromiseDtos.add(promiseDto);
			}
		}

		return UserPromiseListResponseDto.builder()
				.pendingPromises(pendingPromiseDtos)
				.upcomingPromises(upcomingPromiseDtos).build();
	}

	@Transactional
	public FindUserListResponseDto searchUsersByCode(Long userId, String code) {
		List<Object[]> results = userRepository.searchUsersWithFriendStatus(userId, code);
		List<FindUserListResponseDto.FindUserDto> userDtos = new ArrayList<>();
		for (Object[] result : results) {
			User user = (User) result[0];
			boolean isFriend = (Boolean) result[1];
			FindUserListResponseDto.FindUserDto userDto = FindUserListResponseDto.FindUserDto.builder()
					.userId(user.getId())
					.isFriend(isFriend)
					.code(user.getUserId())
					.nickname(user.getNickname())
					.profileImage(user.getProfileImage()).build();
			userDtos.add(userDto);
		}
		return FindUserListResponseDto.builder().users(userDtos).build();
	}


	public MyPageResponseDto getMyPageInfo(User user) {
		/* 유저 정보 */
		MyPageResponseDto.UserDto userDto = MyPageResponseDto.UserDto.builder()
				.nickname(user.getNickname())
				.userId(user.getUserId())
				.userProfile(user.getProfileImage())
				.build();

		/* 시간표 */
		List<UserPromise> userPromises = userPromiseRepository.findAllByUserAndDisplayTrue(user);
		List<Promise> promises = userPromises.stream()
				.map(UserPromise::getPromise)
				.sorted(Comparator.comparing(Promise::getDate)).toList();
		// 현재 날짜 가져오기
		LocalDate today = LocalDate.now();
		// 이번 주 월요일 (한국 시간 기준)
		LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		// 이번 주 일요일
		LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		// promise 중 이번주 약속들
		List<Promise> promises_week = promises.stream()
				.filter(promise -> {
					LocalDate promiseDate = promise.getDate(); // Promise의 날짜 가져오기
					return !promiseDate.isBefore(startOfWeek) && !promiseDate.isAfter(endOfWeek);
				})
				.collect(Collectors.toList());

		// 유저 개인 스케쥴
		List<Schedule> schedules = scheduleRepository.findAllByUser(user);

		List<MyPageResponseDto.ScheduleDto> scheduleDtos = new ArrayList<>();
		for (Promise promise : promises) {
			String placeName = null;
			if (promise.getPlace() != null){
				placeName = promise.getPlace().getName();
			}
			MyPageResponseDto.ScheduleDto scheduleDto = MyPageResponseDto.ScheduleDto.builder()
					.name(promise.getName())
					.dayOfWeek(promise.getDate().getDayOfWeek())
					.timeStart(promise.getTimeStart())
					.timeEnd(promise.getTimeEnd())
					.place(placeName)
					.build();
			scheduleDtos.add(scheduleDto);
		}
		for (Schedule schedule : schedules) {
			MyPageResponseDto.ScheduleDto scheduleDto = MyPageResponseDto.ScheduleDto.builder()
					.name(schedule.getName())
					.dayOfWeek(schedule.getDayOfWeek().toJavaDayOfWeek())
					.timeStart(schedule.getTimeStart())
					.timeEnd(schedule.getTimeEnd())
					.place(schedule.getLocation())
					.build();
			scheduleDtos.add(scheduleDto);
		}

		/* 저장 장소 */
		List<Keyword> Keywords = searchKeywordRepository.findKeywordsByUserId(user.getId());
		List<MyPageResponseDto.SavedPlaceDto> savedPlaceDtos = new ArrayList<>();
		for (Keyword keyword : Keywords) {
			MyPageResponseDto.SavedPlaceDto savedPlaceDto = MyPageResponseDto.SavedPlaceDto.builder()
					.keywordId(keyword.getId())
					.keyword(keyword.getName())
					.build();
			savedPlaceDtos.add(savedPlaceDto);
		}
		/* 내 장소 리뷰 */
		List<Search> searches = searchRepository.findAllByUser(user);
		List<MyPageResponseDto.MyReviewDto> myReviewDtos = new ArrayList<>();
		Place place;
		List<String> keywordNames;
		for (Search search : searches) {
			place = search.getPlace();
			keywordNames = searchKeywordRepository.findKeywordsBySearchId(search.getId())
					.stream()
					.map(Keyword::getName)
					.toList();
			searchKeywordRepository.findKeywordsBySearchId(search.getId());
			MyPageResponseDto.MyReviewDto myReviewDto = MyPageResponseDto.MyReviewDto.builder()
					.placeId(place.getId())
					.name(place.getName())
					.note(search.getNote())
					.score(search.getScore())
					.latitude(place.getLatitude())
					.longitude(place.getLongitude())
					.Keywords(keywordNames)
					.build();
		}


		/* 최종 response */
		return MyPageResponseDto.builder()
				.user(userDto)
				.schedules(scheduleDtos)
				.savedPlaces(savedPlaceDtos)
				.myReviews(myReviewDtos)
				.build();
	}

	public void changeProfileImage(User user, ProfileImageChangeRequestDto profileImageChangeRequestDto) {
		UserProfile userProfile = UserProfile.fromString(profileImageChangeRequestDto.getProfileImage());
		user.setProfileImage(userProfile);
		userRepository.save(user);
	}

	public void deleteUser(User user) {
		try {
			userRepository.delete(user);
		} catch (Exception e) {
			throw new EntityNotFoundException();
		}
	}
}
