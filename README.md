"# arduino-lab-1-team-2" 
"# sp19-lab3-rbp668_sdv454" 

1. Why do embedded systems need a setup and a loop?

The setup is need to configure embedded systems. It's so that you don't have to remind the embedded system what individual pins do over and over.
You set some things up once after reset, and then you have a 'tight' loop runs repeatedly as frequently as possible. The loop function contains the 
code that you want to have repeated over and over again. 

2. Why does our code need to be compiled?

You need a compiler because the computer can only understand machine code. The compiler that understands your code changes it to machine code 
for the computer to understand.

3. When lowering the frequency in step four, what is going wrong? Brainstorm some solutions. Dimmers exist in the real world. What is their solution?

The problem is that at lower frequencies, the eyes can detect the changing duty cycles, making the LED seem like it's flickering. The solution would be 
to use high enough frequencies so that the eyes cannot detect the changing duty cycles. Another solution would be to dynamically calculate the period 
so that the ratio remains at 10. 