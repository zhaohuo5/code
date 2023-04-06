package com.example.ssm.entity.wrapper;

import com.example.ssm.entity.MessageTab;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class RecievedMessage extends MessageTab {
    private List<Integer> tagIds;
    private HashMap groupTag;
    private String typeName;
    private List<Object> comments;
    private Long lctime;
    private Long lmtime;



    public RecievedMessage(MessageTab messageTab,HashMap groupTag,String typeName){


        super(messageTab.getId(),
                messageTab.getAuthorId(),
                messageTab.getTypeId(),
                messageTab.getMsg(),
                messageTab.getCtime(),
                messageTab.getMsgStatus(),
                null
                );
        this.lctime=Timestamp.valueOf(messageTab.getCtime()).getTime();
        this.lmtime=Timestamp.valueOf(messageTab.getMtime()).getTime();
        this.groupTag=groupTag;
        this.typeName=typeName;

    }

    public RecievedMessage(MessageTab messageTab,HashMap groupTag,String typeName,List comments){
        super(messageTab.getId(),
                messageTab.getAuthorId(),
                messageTab.getTypeId(),
                messageTab.getMsg(),
                messageTab.getCtime(),
                messageTab.getMsgStatus(),
                messageTab.getMtime()
        );
        this.groupTag=groupTag;
        this.typeName=typeName;
        this.comments=comments;

    }

}
