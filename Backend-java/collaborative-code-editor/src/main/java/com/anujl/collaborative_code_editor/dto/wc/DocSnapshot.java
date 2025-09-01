package com.anujl.collaborative_code_editor.dto.wc;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class DocSnapshot {
    private String docId;
    private String text;
    private long version;
}

