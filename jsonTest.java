import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
/**
 * @title League of Legends API Project (Temp name till I figure out what information I am trying to find.)
 * @author Prabu Gugagantha
 * @date 1/1/2020
 * @Details: Overall I am trying to learn  more about how api's and json objects work by playing with the League of Legends API.
 * @Goals: I want to be able to retrieve data using League's API using the given information of the users account name and a dev key.
 */
public class jsonTest{
	public static void main(String[] args)throws JSONException, IOException{
		HashMap<String, String> dataSaver = new HashMap<String, String>();
		Scanner consoleReader = new Scanner(System.in);
		String url = introUrlCreator(consoleReader, dataSaver);
		String json = leagueCall(url);
		parser(json, dataSaver);
		String matchHistory = introMatchHistory(consoleReader, dataSaver);
		consoleReader.close();
		System.out.println(matchHistory); // This is the match history
		matchHistoryParser(matchHistory);
	}
	public static void matchHistoryParser(String matchHistory) throws JSONException{
		JSONObject jsonArr = new JSONObject(matchHistory);
		System.out.println(jsonArr.get("matches"));
	}
	/**
	 * @MethodName : introUrlCreator
	 * @Details : Gives the user an intro asking for their dev key and player name.
	 * @param consoleReader : A general scanner to the console
	 * @return url : The url that will be used to retrieve our data. It has a standard format
	 * with a spot for the name of the user and a spot for the dev key
	 */
	public static String introUrlCreator(Scanner consoleReader, HashMap<String, String> dataSaver) {
		System.out.println("Dev Key: ");
		String key = consoleReader.nextLine();
		dataSaver.put("Dev Key", key);
		System.out.println("Player Name: ");
		String name = consoleReader.nextLine();
		dataSaver.put("Player Name", name);
		while(name.indexOf(" ") != -1) {
			name = name.substring(0, name.indexOf(" ")) + "%20" + name.substring(name.indexOf(" ") + 1, name.length());
		}
		//key = "?api_key=RGAPI-11db1a3d-16ff-4461-baec-42ef3ed5ac52";
		String url = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + key;
		//String url2 = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Teemo" + "?api_key=" + key;
		return url;
	}
	/**
	 * @MethodName : introMatchHistory
	 * @Details : The second intro which asks the user if they want the match history for the user.
	 * @param consoleReader : A general scanner to the console
	 * @param summonerAccountId : The summonerAccountID retrieved earlier
	 * @throws IOException : Malformed Url will throw this exception.
	 */
	public static String introMatchHistory(Scanner consoleReader, HashMap<String, String> dataSaver)throws IOException{
		System.out.println("Do you want to access latest match history?");
		String wantLatestMatchHistory = consoleReader.nextLine();
		String matchHistory = "";
		if(wantLatestMatchHistory.equals("yes")) {
			System.out.println("How many matches would you like?");
			String matchAmount = "" + consoleReader.nextInt();
			matchHistory = retrieveMatch(dataSaver, matchAmount);
		}
		return matchHistory;
	}
	/**
	 * @MethodName : leagueCall
	 * @Details : Connects to the given url and retrieves json data which is returned as a string.
	 * @param URL : The URL we will get our data from
	 * @return sb.close() : A string that represents the data we have obtained
	 * @throws IOException : Malformed Url will throw this exception.
	 */
	public static String leagueCall(String URL)throws IOException{
		StringBuilder  sb = new StringBuilder();
		URL url = new URL(URL);
		URLConnection urlConnect = url.openConnection();
		InputStreamReader input = null;
		BufferedReader buffer = null;
		if(urlConnect != null && urlConnect.getInputStream() != null) {
			input = new InputStreamReader(urlConnect.getInputStream());
			buffer = new BufferedReader(input);
			if(buffer != null) {
				int read;
				while((read = buffer.read()) != -1) {
					sb.append((char) read);
				}
			}
		}
		buffer.close();
		input.close();
		return sb.toString();
	}
	/**
	 * @MethodName parser
	 * @Details : Parses through the json string seperating and collecting essential information on the player.
	 * @param json : The string with data on the player.
	 * @return summonerAccountId : This encrypted account Id can be used to pull up the match history of a player.
	 */
	public static void parser(String json, HashMap<String, String> dataSaver)throws JSONException{
		/**json = json.substring(1,json.length()-1);
		String[] summonerInfo = json.split(",");
		String summonerId = (summonerInfo[0]).substring(6,summonerInfo[0].length()-1); // id
		String summonerAccountId = (summonerInfo[1]).substring(13,summonerInfo[1].length()-1); // account ID   this one is important to get match history
		String summonerPuuID = (summonerInfo[2]).substring(9,summonerInfo[2].length()-1); // puuid
		String summonerName = (summonerInfo[3]).substring(8,summonerInfo[3].length()-1); // name of summoner
		String summonerProfileIconId = (summonerInfo[4]).substring(16,summonerInfo[4].length()); // profile icon id
		String summonerRevisionDate = (summonerInfo[5]).substring(15,summonerInfo[5].length()); // revision data
		String summonerLevel = (summonerInfo[6]).substring(16,summonerInfo[6].length()); // summoner level
		dataSaver.put("summonerAccountId", summonerAccountId);
		**/
		JSONObject jsonOb = new JSONObject(json);
		//System.out.println(jsonOb.getString("summonerAccountId"));
		dataSaver.put("summonerAccountId", jsonOb.getString("accountId"));
	}
	/**
	 * @MethodName : retrieveMatch
	 * @Details : Retrieve's the match history of a specified player
	 * @param summonerId
	 * @param matchAmount
	 * @throws IOException
	 */
	public static String retrieveMatch(HashMap<String, String> dataSaver, String matchAmount) throws IOException {

		String web = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" +
					 dataSaver.get("summonerAccountId") + "?endIndex=" + matchAmount + "&beginIndex=0&api_key=" + dataSaver.get("Dev Key");
		URL url = new URL(web);
		URLConnection urlConnect = url.openConnection();
		InputStream input = urlConnect.getInputStream();
		InputStreamReader inputReader = new InputStreamReader(input);
		BufferedReader buffer = new BufferedReader(inputReader);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while((i = buffer.read()) != -1 ) {
			sb = sb.append((char) i);
		}
		return sb.toString();
	}
}
