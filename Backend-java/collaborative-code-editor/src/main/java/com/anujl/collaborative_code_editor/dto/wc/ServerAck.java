package com.anujl.collaborative_code_editor.dto.wc;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class ServerAck {
    private String docId;
    private long version;
    private String type; // ok|resync|error
    private String message;
    private DocSnapshot snapshot; // present when resync


}

