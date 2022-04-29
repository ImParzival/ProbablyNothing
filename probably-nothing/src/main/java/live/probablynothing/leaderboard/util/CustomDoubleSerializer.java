package live.probablynothing.leaderboard.util;

import java.io.IOException;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDoubleSerializer extends JsonSerializer<Double> {

	@Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if(null == value)
		{
			gen.writeNull();
		}
		else
		{
			DecimalFormat df = new DecimalFormat("#,###.00");
			gen.writeNumber(df.format(value)); 
		}
		
	}

}
