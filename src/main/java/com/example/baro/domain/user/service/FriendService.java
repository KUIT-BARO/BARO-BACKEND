package com.example.baro.domain.user.service;

import com.example.baro.common.entity.Friend;
import com.example.baro.common.entity.User;
import com.example.baro.domain.user.repository.FriendRepository;
import com.example.baro.domain.user.repository.UserRepository;
import com.example.baro.domain.user.dto.request.FriendDeleteRequestDto;
import com.example.baro.domain.user.dto.request.FriendRequestDto;
import com.example.baro.domain.user.dto.response.FriendListResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final UserRepository userRepository;
	private final FriendRepository friendRepository;

	public FriendListResponseDto getFriends(User user) {
		List<Friend> friends = friendRepository.findFriendsByUserId(user.getId());
		List<FriendListResponseDto.FriendDto> friendDtos = new ArrayList<>();

		for (Friend friend : friends) {
			User friendUser = friend.getFromUser();
			if (friend.getFromUser().equals(user)) {
				friendUser = friend.getToUser();
			}
			FriendListResponseDto.FriendDto friendDto = FriendListResponseDto.FriendDto.builder()
					.friendId(friendUser.getId())
					.code(friendUser.getUserId())
					.nickname(friendUser.getNickname())
					.profileImage(friendUser.getProfileImage()).build();
			friendDtos.add(friendDto);
		}
		return FriendListResponseDto.builder().friends(friendDtos).build();
	}

	public void requestFriend(User user, FriendRequestDto requestDto) {
		User friend = userRepository.findByUserId(requestDto.getCode()).orElseThrow(EntityNotFoundException::new);
		friendRepository.sendFriendRequest(user, friend);
	}

	public void deleteFriend(User user, FriendDeleteRequestDto requestDto) {
		friendRepository.deleteFriend(user.getId(), requestDto.getFriendId());
	}
}
