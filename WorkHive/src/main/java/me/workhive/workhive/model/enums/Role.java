package me.workhive.workhive.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
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
