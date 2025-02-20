package com.example.baro.domain.user.service;

import com.example.baro.common.entity.User;
import com.example.baro.common.exception.exceptionClass.DuplicateUserException;
import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import com.example.baro.domain.user.repository.FriendRepository;
import com.example.baro.domain.user.repository.UserRepository;
import com.example.baro.domain.user.dto.request.LoginRequestDto;
import com.example.baro.domain.user.dto.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final FriendRepository friendRepository;

	public void signup(SignUpRequestDto requestDto) {
		validateSignupRequest(requestDto);
		// 중복 사용자 확인
		if (userRepository.existsByUserId(requestDto.getId())) {
			throw new DuplicateUserException("same userId exists");
		}

		// 유효성 검사 (예: 비밀번호 길이 체크)
		if (requestDto.getPassword().length() < 8) {
			throw new InvalidRequestException("Password must be at least 8 characters long");
		}

		// 새로운 사용자 저장
		User user = User.builder()
				.userId(requestDto.getId())
				.password(requestDto.getPassword())
				.nickname(requestDto.getNickname())
				.build();

		userRepository.save(user);

		// 사용자 객체 가져오기
		User user15 = userRepository.getReferenceById(15L);
		User user16 = userRepository.getReferenceById(16L);
		User user17 = userRepository.getReferenceById(17L);
		User user18 = userRepository.getReferenceById(18L);
		User user19 = userRepository.getReferenceById(19L);
		User user20 = userRepository.getReferenceById(20L);
		User user21 = userRepository.getReferenceById(21L);

		friendRepository.sendFriendRequest(user, user15);
		friendRepository.sendFriendRequest(user, user16);
		friendRepository.sendFriendRequest(user, user17);
		friendRepository.sendFriendRequest(user, user18);
		friendRepository.sendFriendRequest(user, user19);
		friendRepository.sendFriendRequest(user, user20);
		friendRepository.sendFriendRequest(user, user21);
	}

	public User login(LoginRequestDto requestDto) {
		// 아이디와 비밀번호 검증 (예시: 사용자 조회)
		User user = userRepository.findByUserId(requestDto.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

		// 비밀번호 비교 (실제 비밀번호는 암호화되어 있어야 함)
		if (!user.getPassword().equals(requestDto.getPassword())) {
			throw new IllegalArgumentException("Invalid username or password");
		}

		return user;
	}

	private void validateSignupRequest(SignUpRequestDto requestDto) {
		if (isNullOrEmpty(requestDto.getId()) ||
				isNullOrEmpty(requestDto.getPassword()) ||
				isNullOrEmpty(requestDto.getNickname())) {
			throw new InvalidRequestException("All fields must be provided");
		}
	}

	private boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

}
