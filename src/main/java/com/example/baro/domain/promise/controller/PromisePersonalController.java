package com.example.baro.domain.promise.controller;

import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.place.dto.request.ReviewPostRequestDto;
import com.example.baro.domain.promise.dto.request.PromisePersonalKeywordRequestDto;
import com.example.baro.domain.promise.dto.request.PromisePersonalPlaceRequestDto;
import com.example.baro.domain.promise.dto.request.PromisePersonalTimeRequestDto;
import com.example.baro.domain.promise.service.PromisePersonalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/promise/personal")
public class PromisePersonalController{
    private final PromisePersonalService promisePersonalService;

    @PostMapping("/{promiseId}/times")
    public ApiResponseDto setPersonalTimes(@Valid @RequestBody PromisePersonalTimeRequestDto requestDto, @LoginUser User user, @PathVariable Long promiseId) {
        promisePersonalService.setPersonalTimes(requestDto, user, promiseId);
        return ApiResponseDto.success(SuccessCode.NOTE_CREATE_SUCCESS);
    }


    @PostMapping("/{promiseId}/keywords")
    public ApiResponseDto setPersonalKeywords(@Valid @RequestBody PromisePersonalKeywordRequestDto requestDto, @LoginUser User user, @PathVariable Long promiseId) {
        promisePersonalService.setPersonalKeywords(requestDto, user, promiseId);
        return ApiResponseDto.success(SuccessCode.NOTE_CREATE_SUCCESS);
    }

    @PostMapping("/{promiseId}/places")
    public ApiResponseDto setPersonalPlaces(@Valid @RequestBody PromisePersonalPlaceRequestDto requestDto, @LoginUser User user, @PathVariable Long promiseId) {
        promisePersonalService.setPersonalPlaces(requestDto, user, promiseId);
        return ApiResponseDto.success(SuccessCode.NOTE_CREATE_SUCCESS);
    }

    @PostMapping("/{promiseId}/reject")
    public ApiResponseDto rejectPersonal(@LoginUser User user, @PathVariable Long promiseId) {
        promisePersonalService.rejectPersonal(user, promiseId);
        return ApiResponseDto.success(SuccessCode.NOTE_CREATE_SUCCESS);
    }


}