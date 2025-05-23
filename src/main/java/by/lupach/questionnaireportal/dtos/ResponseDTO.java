package by.lupach.questionnaireportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
//    private Long respondentId;
    private Long questionnaireId;
    private List<ResponseFieldAnswerDTO> answers;
}