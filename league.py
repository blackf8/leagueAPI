import requests

def playerInfo(key):
    print("playerName:")
    playerName = input()

    url = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + playerName +"?api_key=" + key
    response = requests.get(url)
    #print(response.status_code)
    # if 200 we gucci
    player = response.json()
    return player

def playerMatchHis(playerAccountId, key):

    url = "https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + playerAccountId + "?queue=420"+ "&api_key=" + key
    #print(url)
    response = requests.get(url)
    #print(response.status_code)
    # if 200 we gucci
    player = response.json()
    return player

def playerMatchParse(matchHis):
    matches = []
    for match in playerHis[playerHisJsonKeys[0]]:
        if(match["lane"] == 'BOTTOM'):
            matches.append(match["gameId"])
    return matches

def matchAnalysis(matches, key, pName):
    #for match in matches:
    match  = str(matches[0])
    url = "https://na1.api.riotgames.com/lol/match/v4/matches/" + match + "?api_key=" + key
    response = requests.get(url)
    matchJson = response.json()
    partNum = -2
    for part in matchJson['participantIdentities']:
        if(part['player']['summonerName'] == player1['name']):
            partNum = part['participantId']
            print(part['participantId'])

    #print(matchJson['teams'])
    print(matchJson['participants'][partNum - 1].keys())
    print(matchJson['participants'][partNum - 1]['stats'].keys())
    teamID = matchJson['participants'][partNum - 1]['teamId']
    champId = matchJson['participants'][partNum - 1]['championId']
    win = -1
    if(matchJson['teams'][(teamID%100)-1]['win'] == 'Win'):
        win = 1
    else:
        win = 0

    return matchJson

print("key:")
key = input()
player1 = playerInfo(key)
playerJsonKeys = list(player1.keys())
playerHis = playerMatchHis(player1[list(player1.keys())[1]], key)
playerHisJsonKeys = list(playerHis.keys())
matches = playerMatchParse(playerHisJsonKeys)
y = matchAnalysis(matches, key, player1['name'])
yKeys = y.keys()


#['gameId', 'platformId', 'gameCreation', 'gameDuration', 'queueId',
#'mapId', 'seasonId', 'gameVersion', 'gameMode', 'gameType', 'teams', 'participants', 'participantIdentities']
#
#
#
#
