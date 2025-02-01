package com.example.baro.domain.user.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.ErrorCode;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.user.dto.response.FriendListResponseDto;
import com.example.baro.domain.user.dto.request.FriendRequestDto;
import com.example.baro.domain.user.dto.request.FriendDeleteRequestDto;
import com.example.baro.domain.user.service.FriendService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/friends")
@RequiredArgsConstructor
public class FriendController {

	private final FriendService friendService;

	// 친구 목록 조회
	@GetMapping
	public ApiResponseDto<FriendListResponseDto> getFriends(@LoginUser User user) {
		FriendListResponseDto friends = friendService.getFriends(user);
		return ApiResponseDto.success(SuccessCode.FRIEND_DETAIL_GET_SUCCESS, friends);
	}

	// 친구 요청
	@PostMapping("/requests")
	public ApiResponseDto<Void> requestFriend(@RequestBody FriendRequestDto requestDto, @LoginUser User user) {
		try {
			friendService.requestFriend(user, requestDto);
		} catch (EntityNotFoundException e) {
			return ApiResponseDto.fail(ErrorCode.USER_NOT_FOUND);
		}
		return ApiResponseDto.success(SuccessCode.FRIEND_REQUEST_SUCCESS);
	}

	// 친구 삭제
	@DeleteMapping
	public ApiResponseDto<Void> deleteFriend(@RequestBody FriendDeleteRequestDto requestDto, @LoginUser User user) {
		friendService.deleteFriend(user, requestDto);
		return ApiResponseDto.success(SuccessCode.FRIEND_DELETE_SUCCESS);
	}
}