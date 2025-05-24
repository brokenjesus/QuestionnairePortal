package by.lupach.questionnaireportal.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible types of a Field")
public enum FieldType {
    @Schema(description = "Single line text input")
    SINGLE_LINE_TEXT,

    @Schema(description = "Multi-line text input")
    MULTILINE_TEXT,

    @Schema(description = "Radio button group")
    RADIO_BUTTON,

    @Schema(description = "Checkbox group")
    CHECKBOX,

    @Schema(description = "Combo box selection")
    COMBOBOX,

    @Schema(description = "Date picker")
    DATE
}
