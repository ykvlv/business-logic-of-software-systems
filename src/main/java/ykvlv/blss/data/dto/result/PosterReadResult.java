package ykvlv.blss.data.dto.result;

import lombok.Value;

@Value
public class PosterReadResult {
	boolean found;
	boolean success;
	byte[] data;

	public static PosterReadResult notFound() {
		return new PosterReadResult(false, true, null);
	}
	public static PosterReadResult error() {
		return new PosterReadResult(true, false, null);
	}

	public static PosterReadResult success(byte[] data) {
		return new PosterReadResult(true, true, data);
	}
}
