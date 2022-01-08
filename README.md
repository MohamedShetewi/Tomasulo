# Tomasulo
This is a simulator for Tomasulo, which is a famous algorithm in Computer Architecture used for the dynamic scheduling of instructions that allows out-of-order execution.
For more info [check this page](https://en.wikipedia.org/wiki/Tomasulo_algorithm).

## Features
Our simulator accept inputs (MIPS instructions in assembly format) and show step by step how these instructions are executed as well as the content of each 
reservation station/buffer, the register file and the queue. Also, it allows the user to enter the latencies for different instructions. 
Moreover, we allow navigating between different cycles of the execution.

## How to run?
- Clone this project from github
`git clone https://github.com/MohamedShetewi/Tomasulo.git`
- You should have Java 15 installed in your machine.
- Run the main method in the `\Contoller\Controller.java`
