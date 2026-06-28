package me.workhive.workhive.domain.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMINISTRATOR(1),
    RECRUITER(2),
    CANDIDATE(3);

    private final int code;

}
