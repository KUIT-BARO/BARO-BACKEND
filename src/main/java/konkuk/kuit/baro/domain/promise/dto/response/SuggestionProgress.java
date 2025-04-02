package konkuk.kuit.baro.domain.promise.dto.response;

import lombok.Getter;

@Getter
public enum SuggestionProgress {

    NONE("NONE"),
    HALF("HALF"),
    COMPLETE("COMPLETE");

    private final String value;

    SuggestionProgress(String suggestionProgress) {
        this.value = suggestionProgress;
    }
}
