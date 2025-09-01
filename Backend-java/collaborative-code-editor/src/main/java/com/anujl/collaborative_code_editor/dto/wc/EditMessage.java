package com.anujl.collaborative_code_editor.dto.wc;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Setter
@Getter

public class EditMessage {
    @NotBlank private String docId;
    @NotBlank private String clientId;
    @NotNull  private Long baseVersion;
    @NotNull  private List<Op> ops; // ordered

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Op {
        @NotBlank private String type; // insert|delete|replace
        private Integer index;         // for insert/delete/replace
        private Integer length;        // for delete/replace
        private String text;           // for insert/replace
    }
}
