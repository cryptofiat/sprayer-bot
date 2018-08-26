package eu.cryptoeuro.service;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;
import java.io.IOException;
  
@Slf4j
@Component
public class SlackService {  

  @Value("${spray.warning.webhook}")
  private String webhookUrl;

  public void sprayerBalanceLow(Long balance, String address) {

	try {
        new Slack(webhookUrl)
            //.icon(":smiling_imp:") // Ref - http://www.emoji-cheat-sheet.com/
            //.sendToUser("slackbot")
            //.displayName("slack-java-client")
            .push(new SlackMessage("Sprayer low on money. Only €")
            .bold(String.format("%.2f",(float) balance / 100))
            .text(" left on ")
            .code(address)
            .text(".") );
	} catch (IOException ex) {
		log.error("Failed sending slack message: ", ex);
		
	}

  }
   
}  
