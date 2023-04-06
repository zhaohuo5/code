package com.example.ssm.entity.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyQueryWraaper {
    private int page;
    private int userId;
    private int offset;
    private int typeId;
    private int tagId;
    private int role;
}
