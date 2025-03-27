package konkuk.kuit.baro.domain.promise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromiseAvailableTimeResponseDTO {
    List<PromiseMemberDTO> promiseMembers;

    List<List<PromiseMemberAvailableTimeDTO>> promiseAvailableTimes;

}
