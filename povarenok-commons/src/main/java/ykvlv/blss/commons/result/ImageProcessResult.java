package ykvlv.blss.commons.result;

import lombok.Value;

import java.io.Serializable;

@Value
public class ImageProcessResult implements Serializable {
	String uuid;
	byte[] imageData;
}
