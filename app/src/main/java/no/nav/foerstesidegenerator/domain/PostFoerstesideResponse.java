package no.nav.foerstesidegenerator.domain;

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