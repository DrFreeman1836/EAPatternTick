//+------------------------------------------------------------------+
//|                                                     SendTick.mq5 |
//|                                  Copyright 2023, MetaQuotes Ltd. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2023, MetaQuotes Ltd."
#property link      "https://www.mql5.com"
#property version   "1.00"

int TCP_PORT = 8081;
MqlTick latestPrice;
int socket;
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
{
   socket = SocketCreate();
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
{
   SocketClose(socket);
   EventKillTimer();
}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
{
   initOnTick();
   
   if (!checkConnect()) {
      newConnect();
   }
   
   HTTPSend(msgBuilder());

}

string msgBuilder() {
   return StringFormat("{\"ask\": \"%f\" , \"bid\": \"%f\" , \"time\": \"%s\", \"timeMsc\": %s, \"last\": \"%f\", \"volume\": %s, \"volumeReal\": \"%f\", \"flags\": %d}\r\n",
   NormalizeDouble(latestPrice.ask, Digits()),
   NormalizeDouble(latestPrice.bid, Digits()),
   TimeToString(latestPrice.time, TIME_DATE|TIME_MINUTES|TIME_SECONDS),
   IntegerToString(latestPrice.time_msc),
   NormalizeDouble(latestPrice.last, Digits()),
   IntegerToString(latestPrice.volume),
   latestPrice.volume_real,
   latestPrice.flags
   );
}

void HTTPSend(string request) {
   uchar req[];
   int  len = StringToCharArray(request,req) - 1;
   if (len < 0)
      return;
   bool result = SocketSend(socket,req,len) == len;
   if (!result) Print("Ошибка отправки: " + GetLastError());
}

bool checkConnect() {
   return SocketIsConnected(socket);
}

bool newConnect() {
   bool result = SocketConnect(socket, "localhost", TCP_PORT, 1000);
   if (!result) Print("Ошибка соединения: " + GetLastError());
   return result;
}

void initOnTick() {    
   if (!SymbolInfoTick(_Symbol,latestPrice)) {
     Alert("Ошибка получения последних котировок - ошибка:",GetLastError(),"!!");
     return;
   }
}
//+------------------------------------------------------------------+
//| Timer function                                                   |
//+------------------------------------------------------------------+
void OnTimer()
{
   
}
//+------------------------------------------------------------------+
//| BookEvent function                                               |
//+------------------------------------------------------------------+
void OnBookEvent(const string &symbol)
{

}
//+------------------------------------------------------------------+
