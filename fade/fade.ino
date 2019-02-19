void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
}

void dimmer(int freq, int duty){
  int period, onTime, offTime;
  period = 1000/freq;
  onTime = period * duty/100;
  offTime = period - onTime;
  digitalWrite(LED_BUILTIN, HIGH);
  delay(onTime);
  digitalWrite(LED_BUILTIN, LOW);
  delay(offTime);
}

void loop() {
  // put your main code here, to run repeatedly:
  int duty = 0;
  while (duty <= 100){
    dimmer(100, duty);
    duty++;
  }
  while (duty >= 0){
    dimmer(100, duty);
    duty--;
  }
}
