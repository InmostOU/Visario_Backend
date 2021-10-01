package pro.inmost.amazon.chime.model.dto;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {

    private String fileName;
    private String fileType;
    private String messageId;
    private String url;

    public static Metadata makeFromJson(String metadataJson) {
        Gson gson = new Gson();
        return gson.fromJson(metadataJson, Metadata.class);
    }

    @Override
    public String toString() {
        return "{"
                    + "\"fileName\"" + ":" + "\"" + fileName + "\","
                    + "\"fileType\"" + ":" + "\"" + fileType + "\","
                    + "\"messageId\"" + ":" + "\"" + messageId + "\","
                    + "\"url\"" + ":" + "\"" + url + "\"" +
                "}";
    }

    public static void main(String[] args) {

        String json = "{\n" +
                "    \"channelArn\" : \"arn:aws:chime:us-east-1:277431928707:app-instance/c9f0aa1c-74c7-49af-8b75-b650f128511c/channel/23749dc93a441c13e1a885bb9c089ac8162794b39eb497286267b6a1342ab89a\",\n" +
                "    \"content\" : \"hello\",\n" +
                "    \"metadata\" : {\n" +
                "        \"fileName\" : \"File name\",\n" +
                "        \"fileType\" : \"jpg\",\n" +
                "        \"messageId\" : \"AASD-AFSDF-SYUTUT\"\n" +
                "    }\n" +
                "}";

        Gson gson = new Gson();
        MessageDto messageDto = gson.fromJson(json, MessageDto.class);

        System.out.println(messageDto);
    }
}
