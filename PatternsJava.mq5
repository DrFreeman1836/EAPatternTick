//+------------------------------------------------------------------+
//|                                                 PatternsJava.mq5 |
//|                                  Copyright 2023, MetaQuotes Ltd. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2023, MetaQuotes Ltd."
#property link      "https://www.mql5.com"
#property version   "1.00"

#include <JAson.mqh>
input group "Выбор паттернов"
input bool patternActivity = false;
input bool patternPassivity = false;
input bool patternMulti = false;
input bool patternArrow = false;
input group "Параметры активного"
input int time = 1000;
input double count = 25;
input double deltaMaxAsk = 0.00001;
input double deltaMinAsk = 0.00001;
input double deltaMaxBid = 0.00001;
input double deltaMinBid = 0.00001;
input group "Параметры мульти"
input int timeM = 1000;
input int lowLevel = 5;
input int middleLevel = 10;
input int highLevel = 15;
input double deltaMaxAskM = 0.00001;
input double deltaMinAskM = 0.00001;
input double deltaMaxBidM = 0.00001;
input double deltaMinBidM = 0.00001;
input group "Параметры пассивного"
input int timeFirst = 1000;
input int countFirst = 10;
input int timeSecond = 1000;
input int countSecond = 10;
input group "Параметры стрелок"
input int deltaTimeArrow = 1000;
input double kRatio = 10;
input double minAverage = 0.00001;

string URL_PATTERN = "http://localhost/api/v1/ea/signal";
string URL_SETTING = "http://localhost/api/v1/ea/params";
MqlTick latestPrice;

//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
{
   sendSettingsActivity();
   sendSettingsMulti();
   sendSettingsPassivity();
   sendSettingsArrow();
   //EventSetTimer(timeToSendRequest);
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
{
   EventKillTimer();
}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
{
   initOnTick();
   sendRequest();
}
//+------------------------------------------------------------------+
void initOnTick() {
   if (!SymbolInfoTick(_Symbol,latestPrice)) {
     Alert("Ошибка получения последних котировок - ошибка:",GetLastError(),"!!");
     return;
   }
}
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
void createCross(color colors, double price) {
  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES | TIME_SECONDS);
  nameLine += " ";
  nameLine += colors;
  ObjectCreate(0, nameLine, OBJ_ARROW, 0, TimeCurrent(), price);
  ObjectSetInteger(0, nameLine, OBJPROP_ARROWCODE, 251);// установим код стрелки
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, colors);

}

void createCircle(color colors, double price) {
  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES | TIME_SECONDS);
  nameLine += " ";
  nameLine += colors;
  ObjectCreate(0, nameLine, OBJ_ARROW, 0, TimeCurrent(), price);
  ObjectSetInteger(0, nameLine, OBJPROP_ARROWCODE, 161);// установим код круга
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, colors);
}

void createArrowUp(double price) {
  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES | TIME_SECONDS);
  nameLine += " ";
  nameLine += clrBlue;
  ObjectCreate(0, nameLine, OBJ_ARROW_UP, 0, TimeCurrent(), price);
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, clrBlue);
}
void createArrowDown(double price) {
  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES | TIME_SECONDS);
  nameLine += " ";
  nameLine += clrRed;
  ObjectCreate(0, nameLine, OBJ_ARROW_DOWN, 0, TimeCurrent(), price);
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, clrRed);
}
//+------------------------------------------------------------------+
void sendRequest() {

   string url = URL_PATTERN
   + "?activity=" + patternActivity
   + "&passivity=" + patternPassivity
   + "&multi=" + patternMulti
   + "&arrow=" + patternArrow;

   char requestBody[];
   char responseBody[];
   string headersRq;
   int res = WebRequest("GET", url, NULL, 1, requestBody, responseBody, headersRq);
   if (res == -1) {
      Print("Ошибка отправки запроса на паттерны, код ошибки: " + GetLastError());
   }

   CJAVal data;
   bool b = data.Deserialize(responseBody);
   for (int i = 0; i < ArraySize(data["listSignal"].m_e); i++) {
      string type = data["listSignal"].m_e[i]["type"].ToStr();
      int pattern = data["listSignal"].m_e[i]["pattern"].ToInt();
      double price = data["listSignal"].m_e[i]["price"].ToDbl();
      if (type == "activity") {
         designateActivity(pattern, price);
      } else if (type == "passivity") {
         designatePassivity(pattern, price);
      } else if (type == "multi") {
         designateMulty(pattern, price);
      } else if (type == "arrow") {
         designateArrow(pattern, price);
      }
   }

}
//"{"listSignal":[{"type":"activity","pattern":400,"trend":"NO_TREND"},{"type":"passivity","pattern":404,"trend":"NO_TREND"},{"type":"multi","pattern":404,"trend":"NO_TREND"}]}" (length: 173)
//+------------------------------------------------------------------+
void designateActivity(int responseCode, double price) {
  if(responseCode == 200){//если прошла дельта по аску
     createCross(clrRed, price);
     return;
  }
  if(responseCode == 201){//если прошла дельта по бид
    createCross(clrBlue, price);
    return;
  }
  if(responseCode == 202){//если прошли обе дельты цен
    createCross(clrGold, price);
    return;
  }
}

void designatePassivity(int responseCode, double price) {
  if (responseCode == 300) {
     createCircle(clrWhite, price);
  }
}

void designateMulty(int responseCode, double price) {
  if(responseCode == 200){// low level
     createCross(clrGold, price);
     return;
  }
  if(responseCode == 201){// middle level
    createCross(clrBlue, price);
    return;
  }
  if(responseCode == 202){// high level
    createCross(clrRed, price);
    return;
  }
}

void designateArrow(int responseCode, double price) {
  if(responseCode == 200){// ask > bid
     createArrowUp(price);
     return;
  }
  if(responseCode == 201){// bid > ask
    createArrowDown(price);
    return;
  }
}
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
void sendSettingsActivity() {
   string url = URL_SETTING + "/activity"
   + "?time=" + IntegerToString(time)
   + "&count=" + IntegerToString(count)
   + "&deltaMaxAsk=" + DoubleToString(deltaMaxAsk)
   + "&deltaMinAsk=" + DoubleToString(deltaMinAsk)
   + "&deltaMaxBid=" + DoubleToString(deltaMaxBid)
   + "&deltaMinBid=" + DoubleToString(deltaMinBid);

   char requestBody[];
   char responseBody[];
   string headersRq;
   int res = WebRequest("PUT", url, NULL, 2000, requestBody, responseBody, headersRq);
   if (res == -1 || res == 400) {
      Alert("Ошибка установок активного, смотри журнал и лог: " + GetLastError());
      ExpertRemove();
   } else {
      Print("Настройки активного установлены");
   }
}
void sendSettingsMulti() {
   string url = URL_SETTING + "/multi"
   + "?time=" + IntegerToString(timeM)
   + "&lowLevel=" + IntegerToString(lowLevel)
   + "&middleLevel=" + IntegerToString(middleLevel)
   + "&highLevel=" + IntegerToString(highLevel)
   + "&deltaMaxAsk=" + DoubleToString(deltaMaxAskM)
   + "&deltaMinAsk=" + DoubleToString(deltaMinAskM)
   + "&deltaMaxBid=" + DoubleToString(deltaMaxBidM)
   + "&deltaMinBid=" + DoubleToString(deltaMinBidM);

   char requestBody[];
   char responseBody[];
   string headersRq;
   int res = WebRequest("PUT", url, NULL, 2000, requestBody, responseBody, headersRq);
   if (res == -1 || res == 400) {
      Alert("Ошибка установок мульти, смотри журнал и лог: " + GetLastError());
      ExpertRemove();
   } else {
      Print("Настройки мульти установлены");
   }
}
void sendSettingsPassivity() {
   string url = URL_SETTING + "/passivity"
   + "?timeFirst=" + IntegerToString(timeFirst)
   + "&countFirst=" + IntegerToString(countFirst)
   + "&timeSecond=" + IntegerToString(timeSecond)
   + "&countSecond=" + IntegerToString(countSecond);

   char requestBody[];
   char responseBody[];
   string headersRq;
   int res = WebRequest("PUT", url, NULL, 2000, requestBody, responseBody, headersRq);
   if (res == -1 || res == 400) {
      Alert("Ошибка установок пассивного, смотри журнал и лог: " + GetLastError());
      ExpertRemove();
   } else {
      Print("Настройки пассивного установлены");
   }
}
void sendSettingsArrow() {
   string url = URL_SETTING + "/arrow"
   + "?deltaTimeArrow=" + IntegerToString(deltaTimeArrow)
   + "&kRatio=" + DoubleToString(kRatio)
   + "&minAverage=" + DoubleToString(minAverage);

   char requestBody[];
   char responseBody[];
   string headersRq;
   int res = WebRequest("PUT", url, NULL, 2000, requestBody, responseBody, headersRq);
   if (res == -1 || res == 400) {
      Alert("Ошибка установок стрелок, смотри журнал и лог: " + GetLastError());
      ExpertRemove();
   } else {
      Print("Настройки стрелок установлены");
   }
}

/*void OnTimer()
{
   sendRequest();
}*/

//+------------------------------------------------------------------+
//| ChartEvent function                                              |
//+------------------------------------------------------------------+
void OnChartEvent(const int id,
                  const long &lparam,
                  const double &dparam,
                  const string &sparam)
{

}
//+------------------------------------------------------------------+
//| BookEvent function                                               |
//+------------------------------------------------------------------+
void OnBookEvent(const string &symbol)
{

}
//+------------------------------------------------------------------+
