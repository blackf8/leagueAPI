import requests
import time
import pandas as pd
from collections import defaultdict

def playerInfo(key):
    print("playerName:")
    playerName = input()

    url = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + playerName +"?api_key=" + key
    response = requests.get(url)
    #print(response.status_code)
    # if 200 we gucci
    player = response.json()
    return player

def playerMatchHis(playerAccountId, key, begin, end):

    url = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + playerAccountId + "?queue=420"+ "&endIndex="+str(end) + "&beginIndex="+str(begin) + "&api_key=" + key
    #print(url)
    response = requests.get(url)
    #print(response.status_code)
    # if 200 we gucci
    player = response.json()
    return player

def playerMatchParse(playerHis):
    matches = []
    for match in playerHis:
        if(match["lane"] == 'BOTTOM'):
            matches.append(match["gameId"])
    return matches

def matchAnalysis(matches, key, pName):
    data = []
    counter = 0
    keys = []
    for match in matches:

        print(counter)
        counter = counter+1
        match  = str(match)
        url = "https://na1.api.riotgames.com/lol/match/v4/matches/" + match + "?api_key=" + key
        response = requests.get(url)
        matchJson = response.json()
        partNum = -2
        for part in matchJson['participantIdentities']:
            if(part['player']['summonerName'] == player1['name']):
                partNum = part['participantId']


        data.append(list(matchJson['participants'][partNum - 1]['stats'].values()))
        teamID = matchJson['participants'][partNum - 1]['teamId']
        champId = matchJson['participants'][partNum - 1]['championId']
        time.sleep(1)
        if(len(keys) == 0):
            keys = list(matchJson['participants'][partNum - 1]['stats'].keys())
    df = pd.DataFrame(data, columns=keys)
    df.to_excel("league.xlsx")
    print(df)
    return matchJson

print("key:")
key = input()

player1 = playerInfo(key)
playerJsonKeys = list(player1.keys())

print("Matches:")
games = int(input())

print("Beginning: Player Match History Compilation")
compiledPlayerHis = []
for i in range(0,games//100):
    print(i)
    playerHis = playerMatchHis(player1[list(player1.keys())[1]], key, 100*i, (100*i)+100)
    compiledPlayerHis.extend(playerHis['matches'])

playerHis = playerMatchHis(player1[list(player1.keys())[1]], key, 100*(games//100), games)
compiledPlayerHis.extend(playerHis['matches'])

print("Terminating: Player Match History Compilation")
print("Matches Compiled: ", len(compiledPlayerHis))

print("Beginning: Player Match History Parsing")
matches = playerMatchParse(compiledPlayerHis)
print("Terminating: Player Match History Parsing")
print("Matches Parsed: ", len(matches))

print("Beginning: Match Analysis")
y = matchAnalysis(matches, key, player1['name'])
print("Terminating: MatchAnalysis")


#['gameId', 'platformId', 'gameCreation', 'gameDuration', 'queueId',
#'mapId', 'seasonId', 'gameVersion', 'gameMode', 'gameType', 'teams', 'participants', 'participantIdentities']
#
