package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFoerstesideResponse {

	@Schema(
			description = "Førsteside pdf",
			required = false)
	private byte[] foersteside;

	@Schema(
			description = "Løpenummer for førsteside",
			required = false)
	private String loepenummer;
}