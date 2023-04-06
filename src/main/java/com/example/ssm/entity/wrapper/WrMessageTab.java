package com.example.ssm.entity.wrapper;

import com.example.ssm.entity.MessageTab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrMessageTab {
    private List<? extends  MessageTab> messageTab;


}
