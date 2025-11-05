package com.sparta.userservice.application.dto.request;

import com.sparta.userservice.domain.enums.UserRoleEnum;
import com.sparta.userservice.domain.enums.UserStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserCreateRequest {

    private UserRoleEnum role;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min = 4, max = 10, message = "사용자 이름은 4~10자여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 소문자(a~z)와 숫자(0~9)만 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank
    private String slackId;

    private UserStatusEnum status;

}
