void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
  int duty_g = 5;
  int freq_g = 100;
  int deltaDuty = 5;
}
void dimmer(int freq, int duty){
  int period. onTime, offTime;
  period = 1000/freq;               //delay() is in milliseconds
  onTime=period*duty/100;           //how to pretend duty is a fraction
  offTime = period - onTime;        //all time is accounted for(none lost to rounding)
  digitalWrite(LED_BUILTIN, HIGH);  // turn the LED on (HIGH is the voltage level)
  delay(onTime);                    // wait for the interval
  digitalWrite(LED_BUILTIN,LOW);    // turn the LED off by making the voltage LOW
  delay(offTime);                   // wait for 10ms
}
void loop() {
  // put your main code here, to run repeatedly:
  dimmer(freq_g, duty_g);
  while(duty_g<=freq_g){
    dimmer(freq_g, duty_g);
    duty_g+=deltaDuty;
  }
  while(duty_g>=0){
    dimmer(freq_g, duty_g);
    duty_g-=deltaDuty;
  }

}
