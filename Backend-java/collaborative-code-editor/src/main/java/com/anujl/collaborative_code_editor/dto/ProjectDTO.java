package com.anujl.collaborative_code_editor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProjectDTO {
    private String id;
    private String name;
    private String language;
    private  String author;
    private List<String> codeContent;

    @Override
   public String toString() {
        return "ProjectDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", codeContent=" + codeContent +
                '}';
    }

}
